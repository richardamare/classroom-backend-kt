package com.richardamare.classroombackend.semester

import com.richardamare.classroombackend.core.annotation.TenantId
import com.richardamare.classroombackend.semester.params.SemesterCreateParams
import com.richardamare.classroombackend.semester.requests.SemesterCreateRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("/api/v1/semesters")
class SemesterController(
    private val semesterService: SemesterService
) {

    @PostMapping
    fun createSemester(
        @RequestBody @Valid body: SemesterCreateRequest,
        @TenantId tenantId: String,
    ): ResponseEntity<*> {

        val startDate = ZonedDateTime.parse(body.startDate)
        val endDate = ZonedDateTime.parse(body.endDate)

        val res = semesterService.createSemester(
            SemesterCreateParams(
                name = body.name,
                startDate = startDate.toLocalDateTime(),
                endDate = endDate.toLocalDateTime(),
                tenantId = tenantId
            )
        )

        return ResponseEntity.ok(res)
    }
}