package com.richardamare.classroombackend.course.dto

import com.richardamare.classroombackend.course.CourseType

data class CourseListDTO(
    val id: String,
    val name: String,
    val description: String,
    val type: CourseType,
    val teacher: Teacher,
    val group: Group,
) {
    data class Teacher(
        val id: String,
        val fullName: String,
    )

    data class Group(
        val id: String,
        val name: String,
    )
}
