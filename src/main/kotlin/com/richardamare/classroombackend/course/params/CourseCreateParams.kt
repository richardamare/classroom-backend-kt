package com.richardamare.classroombackend.course.params

import com.richardamare.classroombackend.course.CourseType

data class CourseCreateParams(
    val tenantId: String,
    val semesterId: String,
    val studentGroupId: String,
    val teacherId: String,
    val name: String,
    val description: String,
    val type: CourseType,
)
