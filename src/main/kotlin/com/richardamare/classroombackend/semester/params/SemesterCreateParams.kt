package com.richardamare.classroombackend.semester.params

import java.time.ZonedDateTime

data class SemesterCreateParams(
    val name: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val tenantId: String,
)
