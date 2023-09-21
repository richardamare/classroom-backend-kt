package com.richardamare.classroombackend.studentgroup.request

import jakarta.validation.constraints.NotBlank

data class StudentGroupCreateRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Semester ID is required")
    val semesterId: String,
)