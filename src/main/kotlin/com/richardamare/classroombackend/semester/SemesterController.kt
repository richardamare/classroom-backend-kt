package com.richardamare.classroombackend.semester

import com.richardamare.classroombackend.core.annotation.TenantId
import com.richardamare.classroombackend.semester.params.SemesterCreateParams
import com.richardamare.classroombackend.semester.request.SemesterCreateRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime

@RestController
@RequestMapping("/api/v1/semesters")
class SemesterController(
    private val semesterService: SemesterService
) {

    @PostMapping
    @Secured("ROLE_OFFICE")
    fun createSemester(
        @RequestBody @Valid body: SemesterCreateRequest,
        @TenantId tenantId: String,
    ): ResponseEntity<*> {

        val startDate = ZonedDateTime.parse(body.startDate).toLocalDateTime()
        val endDate = ZonedDateTime.parse(body.endDate).toLocalDateTime()

        val res = semesterService.createSemester(
            SemesterCreateParams(
                name = body.name,
                startDate = startDate,
                endDate = endDate,
                tenantId = tenantId
            )
        )

        return ResponseEntity.ok(res)
    }

    @GetMapping
    fun listSemesters(
        @TenantId tenantId: String,
    ): ResponseEntity<*> {
        val res = semesterService.listSemesters(tenantId)
        return ResponseEntity.ok(res)
    }

    @GetMapping("/{id}")
    @Secured("ROLE_OFFICE")
    fun getSemester(
        @TenantId tenantId: String,
        @PathVariable id: String,
    ): ResponseEntity<*> {
        val res = semesterService.getSemester(tenantId, id)
        return ResponseEntity.ok(res)
    }
}