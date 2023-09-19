package com.richardamare.classroombackend.auth

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

        val user = userRepository.findById(username).orElseThrow {
            Exception("User not found")
        }

        return UsernamePasswordAuthenticationToken(
            user.id,
            null,
            listOf(org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_${user.role.name.lowercase()}")),
        )
    }

    override fun findUserById(id: String): AuthUser {
        val user = userRepository.findById(id).orElseThrow {
            Exception("User not found")
        }

        return AuthUser(user)
    }
}