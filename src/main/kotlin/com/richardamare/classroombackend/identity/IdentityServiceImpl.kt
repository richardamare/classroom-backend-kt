package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.auth.AuthUser
import com.richardamare.classroombackend.auth.JwtUtil
import com.richardamare.classroombackend.core.exception.ResourceNotFoundException
import com.richardamare.classroombackend.core.exception.UnauthorizedException
import com.richardamare.classroombackend.identity.params.LoginParams
import com.richardamare.classroombackend.identity.params.UserAdminCreateParams
import com.richardamare.classroombackend.identity.params.UserOfficeCreateParams
import com.richardamare.classroombackend.identity.result.LoginResult
import com.richardamare.classroombackend.tenant.TenantRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class IdentityServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val tenantRepository: TenantRepository,
) : IdentityService {

    private val logger = LoggerFactory.getLogger(IdentityServiceImpl::class.java)

    private fun generateRandomPassword(len: Int = 10): String {
        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        return (1..len)
            .map { chars.random() }
            .joinToString("")
    }

    override fun createAdminUser(params: UserAdminCreateParams): String {
        val hashedPassword = try {
            passwordEncoder.encode(params.password)
        } catch (e: Exception) {
            logger.error("Error hashing password", e)
            throw IllegalStateException()
        }

        val existingUsers = try {
            userRepository.findAllByEmailAndRoleAndTenantId(
                email = params.email,
                role = UserRole.ADMIN,
                tenantId = null,
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
                    tenantId = null,
                    role = UserRole.ADMIN,
                )
            )

            user.id.toHexString()
        } catch (e: Exception) {
            logger.error("Error creating user", e)
            throw IllegalStateException()
        }
    }

    override fun createOfficeUser(params: UserOfficeCreateParams): String {
        val password = generateRandomPassword()

        val tenant = tenantRepository.findById(params.tenantId)
            .orElseThrow { ResourceNotFoundException("Tenant not found") }

        val hashedPassword = try {
            passwordEncoder.encode(password)
        } catch (e: Exception) {
            logger.error("Error hashing password", e)
            throw IllegalStateException()
        }

        val existingUsers = userRepository.findAllByEmailAndRoleAndTenantId(
            email = params.email,
            role = UserRole.OFFICE,
            tenantId = tenant._id,
        )

        if (existingUsers.isNotEmpty()) throw IllegalArgumentException("User already exists")

        return try {
            val user = userRepository.save(
                User(
                    firstName = params.firstName,
                    lastName = params.lastName,
                    email = params.email,
                    password = hashedPassword,
                    tenantId = tenant.id,
                    role = UserRole.OFFICE,
                )
            )

            // TODO: send user.office.created event to send email to user with password

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

        val claims = mutableMapOf<String, Any>(
            "role" to user.role.name.lowercase(),
        )

        if (user.tenantId != null) claims["tenantId"] = user.tenantId!!

        val token = jwtUtil.generateToken(
            userDetails = AuthUser(user),
            extraClaims = claims,
        )

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