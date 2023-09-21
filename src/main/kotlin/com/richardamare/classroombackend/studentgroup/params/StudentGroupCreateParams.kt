package com.richardamare.classroombackend.studentgroup.params

import com.richardamare.classroombackend.studentgroup.StudentGroupType

data class StudentGroupCreateParams(
    val name: String,
    val tenantId: String,
    val semesterId: String,
    val type: StudentGroupType,
)
