package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.identity.params.UserCreateParams
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class IdentityServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : IdentityService {

    private val logger = LoggerFactory.getLogger(IdentityServiceImpl::class.java)

    override fun createUser(params: UserCreateParams): String {

        val hashedPassword = try {
            passwordEncoder.encode(params.password)
        } catch (e: Exception) {
            logger.error("Error hashing password", e)
            throw e
        }

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
            throw e
        }
    }
}