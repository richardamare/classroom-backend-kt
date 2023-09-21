package com.richardamare.classroombackend.identity.params

data class UserParentCreateParams(
    val firstName: String,
    val lastName: String,
    val email: String,
    val tenantId: String,
    val studentId: String,
)
