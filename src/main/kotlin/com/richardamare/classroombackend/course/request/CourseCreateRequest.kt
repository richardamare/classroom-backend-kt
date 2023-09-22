package com.richardamare.classroombackend.course.request

import jakarta.validation.constraints.NotBlank

data class CourseCreateRequest(
    @field:NotBlank(message = "Semester ID is required")
    val semesterId: String,
    @field:NotBlank(message = "Student Group ID is required")
    val studentGroupId: String,
    @field:NotBlank(message = "Teacher ID is required")
    val teacherId: String,
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Description is required")
    val description: String,
    @field:NotBlank(message = "Type is required")
    val type: String,
)
