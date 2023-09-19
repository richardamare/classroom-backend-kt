package com.richardamare.classroombackend.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

interface AuthService {
    fun authorize(token: String): UsernamePasswordAuthenticationToken?
    fun findUserById(id: String): AuthUser
}