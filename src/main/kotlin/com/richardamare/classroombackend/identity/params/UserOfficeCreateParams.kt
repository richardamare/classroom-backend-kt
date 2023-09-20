package com.richardamare.classroombackend.identity.params

data class UserOfficeCreateParams(
    val email: String,
    val firstName: String,
    val lastName: String,
    val tenantId: String,
)
