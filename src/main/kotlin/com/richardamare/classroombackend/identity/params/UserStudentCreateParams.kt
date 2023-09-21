package com.richardamare.classroombackend.identity.params

data class UserStudentCreateParams(
    val email: String,
    val firstName: String,
    val lastName: String,
    val tenantId: String,
)
