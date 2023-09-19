package com.richardamare.classroombackend.auth

import com.richardamare.classroombackend.core.exception.UnauthorizedException
import com.richardamare.classroombackend.identity.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
) : AuthService {
    override fun authorize(token: String): UsernamePasswordAuthenticationToken? {
        val username = jwtUtil.extractUsername(token) ?: return null

        val user = userRepository.findById(username)
            .orElseThrow { UnauthorizedException() }

        return UsernamePasswordAuthenticationToken(user.id, null, AuthUser(user).authorities)
    }

    override fun findUserById(id: String): AuthUser {
        val user = userRepository.findById(id)
            .orElseThrow { UnauthorizedException() }

        return AuthUser(user)
    }
}