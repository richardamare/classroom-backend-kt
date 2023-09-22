package com.richardamare.classroombackend.identity.dto

import com.richardamare.classroombackend.identity.UserRole

data class UserDTO(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
    val isVerified: Boolean,
    val studentId: String? = null,
    val tenantId: String? = null,
    val initials: String = if (firstName.isNotEmpty() && lastName.isNotEmpty()) "${firstName[0]}${lastName[0]}" else "",
)
