package com.richardamare.classroombackend.identity.request

import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length

data class UserOfficeCreateRequest(
    @field:Email(message = "Email must be valid")
    val email: String,
    @field:Length(min = 2, message = "First name must be at least 2 characters long")
    val firstName: String,
    @field:Length(min = 2, message = "Last name must be at least 2 characters long")
    val lastName: String,
)
