package com.richardamare.classroombackend.identity.params

data class UserTeacherCreateParams(
    val firstName: String,
    val lastName: String,
    val email: String,
    val tenantId: String,
)
