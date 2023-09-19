package com.richardamare.classroombackend.identity.requests

import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length

data class RegisterAdminRequest(
    @field:Email(message = "Email must be valid")
    val email: String,
    @field:Length(min = 8, message = "Password must be at least 8 characters long")
    val password: String,
    @field:Length(min = 2, message = "First name must be at least 2 characters long")
    val firstName: String,
    @field:Length(min = 2, message = "Last name must be at least 2 characters long")
    val lastName: String,
)
