package com.richardamare.classroombackend.studentgroup.params

data class StudentGroupAddStudentParams(
    val tenantId: String,
    val studentGroupId: String,
    val studentId: String,
)
