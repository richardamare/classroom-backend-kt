package com.richardamare.classroombackend.semester.requests

import jakarta.validation.constraints.NotBlank

data class SemesterCreateRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Start date is required")
    val startDate: String,
    @field:NotBlank(message = "End date is required")
    val endDate: String,
)
