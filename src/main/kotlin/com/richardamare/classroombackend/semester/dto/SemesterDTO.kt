package com.richardamare.classroombackend.semester.dto

import java.time.LocalDateTime

data class SemesterDTO(
    val id: String,
    val name: String,
    val tenantId: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
)
