package com.richardamare.classroombackend.auth

import com.richardamare.classroombackend.core.exception.UnauthorizedException
import com.richardamare.classroombackend.identity.UserRepository
import io.jsonwebtoken.ExpiredJwtException
import org.bson.types.ObjectId
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
) : AuthService {
    override fun authorize(token: String): UsernamePasswordAuthenticationToken? {
        val username = try {
            jwtUtil.extractUsername(token) ?: return null
        } catch (e: ExpiredJwtException) {
            throw UnauthorizedException("Token expired")
        }

        ObjectId.isValid(username).takeIf { it } ?: return null

        val user = userRepository.findById(ObjectId(username))
            .orElseThrow { UnauthorizedException() }

        return UsernamePasswordAuthenticationToken(user.id.toHexString(), null, AuthUser(user).authorities)
    }

    override fun findUserById(id: String): AuthUser {
        val user = userRepository.findById(ObjectId(id))
            .orElseThrow { UnauthorizedException() }

        return AuthUser(user)
    }
}