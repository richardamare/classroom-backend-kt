package com.richardamare.classroombackend.studentgroup.params

data class StudentGroupCreateParams(
    val name: String,
    val tenantId: String,
    val semesterId: String,
)
