package com.richardamare.classroombackend.semester.dto

import java.time.ZonedDateTime

data class SemesterDTO(
    val id: String,
    val name: String,
    val tenantId: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
)
