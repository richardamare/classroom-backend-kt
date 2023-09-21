package com.richardamare.classroombackend.studentgroup

import com.richardamare.classroombackend.core.annotation.TenantId
import com.richardamare.classroombackend.studentgroup.params.StudentGroupCreateParams
import com.richardamare.classroombackend.studentgroup.request.StudentGroupCreateRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/student-groups")
class StudentGroupController(
    private val studentGroupService: StudentGroupService,
) {
    @PostMapping
    fun createStudentGroup(
        @RequestBody @Valid body: StudentGroupCreateRequest,
        @TenantId tenantId: String,
    ): ResponseEntity<*> {
        val res = studentGroupService.createStudentGroup(
            StudentGroupCreateParams(
                name = body.name,
                tenantId = tenantId,
                semesterId = body.semesterId,
                type = StudentGroupType.valueOf(body.type),
            )
        )

        return ResponseEntity.ok(res)
    }
}