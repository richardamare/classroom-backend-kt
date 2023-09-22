package com.richardamare.classroombackend.studentgroup

import com.richardamare.classroombackend.core.annotation.TenantId
import com.richardamare.classroombackend.studentgroup.params.StudentGroupAddStudentParams
import com.richardamare.classroombackend.studentgroup.params.StudentGroupCreateParams
import com.richardamare.classroombackend.studentgroup.params.StudentGroupRemoveStudentParams
import com.richardamare.classroombackend.studentgroup.request.StudentGroupCreateRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/student-groups")
class StudentGroupController(
    private val studentGroupService: StudentGroupService,
) {
    @PostMapping
    @Secured("ROLE_OFFICE")
    fun createStudentGroup(
        @RequestBody @Valid body: StudentGroupCreateRequest,
        @TenantId tenantId: String,
    ): ResponseEntity<*> {
        val res = studentGroupService.createStudentGroup(
            StudentGroupCreateParams(
                name = body.name,
                tenantId = tenantId,
                semesterId = body.semesterId,
                type = StudentGroupType.valueOf(body.type.uppercase()),
            )
        )

        return ResponseEntity.ok(res)
    }

    @PostMapping("/{id}/students/{studentId}")
    @Secured("ROLE_OFFICE")
    fun addStudentToGroup(
        @PathVariable("id") id: String,
        @PathVariable("studentId") studentId: String,
        @TenantId tenantId: String,
    ): ResponseEntity<Unit> {

        studentGroupService.addStudentToGroup(
            StudentGroupAddStudentParams(
                studentGroupId = id,
                studentId = studentId,
                tenantId = tenantId,
            )
        )

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}/students/{studentId}")
    @Secured("ROLE_OFFICE")
    fun removeStudentFromGroup(
        @PathVariable("id") id: String,
        @PathVariable("studentId") studentId: String,
        @TenantId tenantId: String,
    ): ResponseEntity<Unit> {

        studentGroupService.removeStudentFromGroup(
            StudentGroupRemoveStudentParams(
                studentGroupId = id,
                studentId = studentId,
                tenantId = tenantId,
            )
        )

        return ResponseEntity.ok().build()
    }
}