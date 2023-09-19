package com.richardamare.classroombackend.identity.requests

import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length

data class LoginRequest(
    @field:Email(message = "Email must be valid")
    val email: String,
    @field:Length(min = 8, message = "Password must be at least 8 characters long")
    val password: String,
)
