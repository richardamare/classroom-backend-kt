package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.auth.AuthUser
import com.richardamare.classroombackend.auth.JwtUtil
import com.richardamare.classroombackend.core.exception.ResourceNotFoundException
import com.richardamare.classroombackend.core.exception.UnauthorizedException
import com.richardamare.classroombackend.identity.dto.UserDTO
import com.richardamare.classroombackend.identity.params.*
import com.richardamare.classroombackend.identity.result.LoginResult
import com.richardamare.classroombackend.identity.result.RegisterResult
import com.richardamare.classroombackend.tenant.TenantRepository
import org.bson.types.ObjectId
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
//        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
//        return (1..len)
//            .map { chars.random() }
//            .joinToString("")
        return "password"
    }

    override fun createAdminUser(params: UserAdminCreateParams): RegisterResult {
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

            RegisterResult(
                id = user.id.toHexString(),
            )
        } catch (e: Exception) {
            logger.error("Error creating user", e)
            throw IllegalStateException()
        }
    }

    override fun createOfficeUser(params: UserOfficeCreateParams): RegisterResult {
//        val password = generateRandomPassword()
        val password = "password" // TODO: remove this

        val tenant = tenantRepository.findById(ObjectId(params.tenantId))
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
            tenantId = tenant.id,
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

            // TODO: send tenant.user.created event to send email to user with password

            RegisterResult(
                id = user.id.toHexString(),
            )
        } catch (e: Exception) {
            logger.error("Error creating user", e)
            throw IllegalStateException()
        }
    }

    override fun getUserById(id: String): UserDTO? {
        val user = userRepository.findById(ObjectId(id))
            .orElseThrow { ResourceNotFoundException("User not found") }

        return UserDTO(
            id = user.id.toHexString(),
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            role = user.role,
            isVerified = user.isVerified,
            studentId = user.studentId?.toHexString(),
            tenantId = user.tenantId?.toHexString(),
        )
    }

    override fun login(params: LoginParams): LoginResult {

        val tenant = if (params.tenantId != null) {
            tenantRepository.findById(ObjectId(params.tenantId))
                .orElseThrow { ResourceNotFoundException("Tenant not found") }
        } else null

        val user = userRepository.findByEmailAndRoleAndTenantId(
            email = params.email,
            role = params.role,
            tenantId = tenant?.id,
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

    override fun createTeacherUser(params: UserTeacherCreateParams): RegisterResult {
        val tenant = tenantRepository.findById(ObjectId(params.tenantId))
            .orElseThrow { ResourceNotFoundException("Tenant not found") }

        val existingUsers = userRepository.findAllByEmailAndRoleAndTenantId(
            email = params.email,
            role = UserRole.TEACHER,
            tenantId = tenant.id,
        )

        if (existingUsers.isNotEmpty()) throw IllegalArgumentException("User already exists")

        val password = generateRandomPassword()

        val hashedPassword = try {
            passwordEncoder.encode(password)
        } catch (e: Exception) {
            logger.error("Error hashing password", e)
            throw IllegalStateException()
        }

        return try {
            val user = userRepository.save(
                User(
                    firstName = params.firstName,
                    lastName = params.lastName,
                    email = params.email,
                    password = hashedPassword,
                    tenantId = tenant.id,
                    role = UserRole.TEACHER,
                )
            )

            // TODO: send tenant.user.created event to send email to user with password

            RegisterResult(
                id = user.id.toHexString(),
            )
        } catch (e: Exception) {
            logger.error("Error creating user", e)
            throw IllegalStateException()
        }
    }

    override fun createStudentUser(params: UserStudentCreateParams): RegisterResult {
        val tenant = tenantRepository.findById(ObjectId(params.tenantId))
            .orElseThrow { ResourceNotFoundException("Tenant not found") }

        val existingUsers = userRepository.findAllByEmailAndRoleAndTenantId(
            email = params.email,
            role = UserRole.STUDENT,
            tenantId = tenant.id,
        )

        if (existingUsers.isNotEmpty()) throw IllegalArgumentException("User already exists")

        val password = generateRandomPassword()

        val hashedPassword = try {
            passwordEncoder.encode(password)
        } catch (e: Exception) {
            logger.error("Error hashing password", e)
            throw IllegalStateException()
        }

        return try {
            val user = userRepository.save(
                User(
                    firstName = params.firstName,
                    lastName = params.lastName,
                    email = params.email,
                    password = hashedPassword,
                    tenantId = tenant.id,
                    role = UserRole.STUDENT,
                )
            )

            // TODO: send tenant.user.created event to send email to user with password

            RegisterResult(
                id = user.id.toHexString(),
            )
        } catch (e: Exception) {
            logger.error("Error creating user", e)
            throw IllegalStateException()
        }
    }

    override fun createParentUser(params: UserParentCreateParams): RegisterResult {
        val tenant = tenantRepository.findById(ObjectId(params.tenantId))
            .orElseThrow { ResourceNotFoundException("Tenant not found") }

        val existingUsers = userRepository.findAllByEmailAndRoleAndTenantId(
            email = params.email,
            role = UserRole.PARENT,
            tenantId = tenant.id,
        )

        if (existingUsers.isNotEmpty()) throw IllegalArgumentException("User already exists")

        val password = generateRandomPassword()

        val hashedPassword = try {
            passwordEncoder.encode(password)
        } catch (e: Exception) {
            logger.error("Error hashing password", e)
            throw IllegalStateException()
        }

        val student = try {
            userRepository.findByIdAndTenantId(ObjectId(params.studentId), tenant.id)
                ?: throw IllegalArgumentException("Student not found")
        } catch (e: Exception) {
            logger.error("Error finding student", e)
            throw IllegalStateException()
        }

        if (student.role != UserRole.STUDENT) throw IllegalArgumentException("User is not a student")

        return try {
            val user = userRepository.save(
                User(
                    firstName = params.firstName,
                    lastName = params.lastName,
                    email = params.email,
                    password = hashedPassword,
                    tenantId = tenant.id,
                    role = UserRole.PARENT,
                    studentId = student.id,
                )
            )

            // TODO: send tenant.user.created event to send email to user with password

            RegisterResult(
                id = user.id.toHexString(),
            )
        } catch (e: Exception) {
            logger.error("Error creating user", e)
            throw IllegalStateException()
        }
    }
}