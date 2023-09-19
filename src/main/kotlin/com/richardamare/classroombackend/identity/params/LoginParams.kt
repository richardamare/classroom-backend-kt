package com.richardamare.classroombackend.identity.params

import com.richardamare.classroombackend.identity.UserRole

data class LoginParams(
    val email: String,
    val password: String,
    val role: UserRole,
    val tenantId: String?,
)