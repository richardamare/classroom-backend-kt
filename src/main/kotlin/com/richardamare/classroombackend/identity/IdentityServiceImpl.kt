package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.auth.AuthUser
import com.richardamare.classroombackend.auth.JwtUtil
import com.richardamare.classroombackend.core.exception.ResourceNotFoundException
import com.richardamare.classroombackend.core.exception.UnauthorizedException
import com.richardamare.classroombackend.identity.params.LoginParams
import com.richardamare.classroombackend.identity.params.UserCreateParams
import com.richardamare.classroombackend.identity.result.LoginResult
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class IdentityServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
) : IdentityService {

    private val logger = LoggerFactory.getLogger(IdentityServiceImpl::class.java)

    override fun createUser(params: UserCreateParams): String {
        val hashedPassword = try {
            passwordEncoder.encode(params.password)
        } catch (e: Exception) {
            logger.error("Error hashing password", e)
            throw IllegalStateException()
        }

        val existingUsers = try {
            userRepository.findAllByEmailAndRoleAndTenantId(
                email = params.email,
                role = params.role,
                tenantId = params.tenantId,
            )
        } catch (e: Exception) {
            logger.error("Error finding user", e)
            throw IllegalStateException()
        }

        if (existingUsers.isNotEmpty()) throw IllegalArgumentException("User already exists")

        return try {
            val user = userRepository.save(
                User(
                    firstName = params.firstName,
                    lastName = params.lastName,
                    email = params.email,
                    password = hashedPassword,
                    tenantId = params.tenantId,
                    role = params.role,
                )
            )

            user.id.toHexString()
        } catch (e: Exception) {
            logger.error("Error creating user", e)
            throw IllegalStateException()
        }
    }

    override fun login(params: LoginParams): LoginResult {
        val user = userRepository.findByEmailAndRoleAndTenantId(
            email = params.email,
            role = params.role,
            tenantId = params.tenantId,
        ).orElseThrow {
            ResourceNotFoundException("No account found with this email")
        }

        if (!user.isVerified) throw UnauthorizedException("Not verified")

        if (!passwordEncoder.matches(params.password, user.password))
            throw UnauthorizedException("Invalid credentials")


        val token = when {
            user.tenantId != null -> jwtUtil.generateToken(
                userDetails = AuthUser(user),
                extraClaims = mapOf("tenantId" to user.tenantId!!),
            )

            else -> jwtUtil.generateToken(userDetails = AuthUser(user))
        }

        if (token == null) {
            logger.error("Error generating token")
            throw IllegalStateException()
        }

        return LoginResult(
            accessToken = token,
            expiresAt = Date(System.currentTimeMillis() + JwtUtil.expirationTime).time,
        )
    }
}