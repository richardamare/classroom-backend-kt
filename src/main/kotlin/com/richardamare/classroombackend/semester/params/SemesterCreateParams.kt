package com.richardamare.classroombackend.semester.params

import java.time.LocalDateTime

data class SemesterCreateParams(
    val name: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val tenantId: String,
)
