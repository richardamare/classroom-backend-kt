package com.richardamare.classroombackend.studentgroup.params

data class StudentGroupRemoveStudentParams(
    val tenantId: String,
    val studentGroupId: String,
    val studentId: String,
)
