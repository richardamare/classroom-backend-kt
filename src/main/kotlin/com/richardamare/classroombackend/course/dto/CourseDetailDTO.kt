package com.richardamare.classroombackend.course.dto

import com.richardamare.classroombackend.course.CourseType

data class CourseDetailDTO(
    val id: String,
    val name: String,
    val description: String,
    val type: CourseType,
    val teacher: Teacher,
    val group: Group,
    val students: List<Student>,
) {
    data class Teacher(
        val id: String,
        val fullName: String,
    )

    data class Group(
        val id: String,
        val name: String,
    )

    data class Student(
        val id: String,
        val fullName: String,
    )
}

