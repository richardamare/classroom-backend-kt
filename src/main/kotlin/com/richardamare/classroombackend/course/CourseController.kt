package com.richardamare.classroombackend.course

import com.richardamare.classroombackend.auth.AuthUser
import com.richardamare.classroombackend.core.annotation.CurrentUser
import com.richardamare.classroombackend.core.annotation.TenantId
import com.richardamare.classroombackend.course.params.CourseCreateParams
import com.richardamare.classroombackend.course.params.CourseDetailParams
import com.richardamare.classroombackend.course.params.CourseListParams
import com.richardamare.classroombackend.course.request.CourseCreateRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/courses")
class CourseController(
    private val courseService: CourseService
) {
    @PostMapping
    @Secured("ROLE_OFFICE")
    fun createCourse(
        @RequestBody @Valid body: CourseCreateRequest,
        @TenantId tenantId: String
    ): ResponseEntity<*> {
        val res = courseService.createCourse(
            CourseCreateParams(
                tenantId = tenantId,
                semesterId = body.semesterId,
                studentGroupId = body.studentGroupId,
                teacherId = body.teacherId,
                name = body.name,
                description = body.description,
                type = CourseType.valueOf(body.type.uppercase()),
            )
        )

        return ResponseEntity.ok(res)
    }

    @GetMapping
    @Secured("ROLE_STUDENT", "ROLE_TEACHER", "ROLE_OFFICE", "ROLE_PARENT")
    fun listCourses(
        @TenantId tenantId: String,
        @CurrentUser user: AuthUser,
    ): ResponseEntity<*> {
        val res = courseService.listCourses(
            CourseListParams(
                tenantId = tenantId,
                userId = user.id,
            )
        )

        return ResponseEntity.ok(res.courses)
    }

    @GetMapping("/{id}")
    @Secured("ROLE_STUDENT", "ROLE_TEACHER", "ROLE_OFFICE", "ROLE_PARENT")
    fun getCourse(
        @TenantId tenantId: String,
        @CurrentUser user: AuthUser,
        @PathVariable id: String,
    ): ResponseEntity<*> {
        val res = courseService.getCourse(
            CourseDetailParams(
                tenantId = tenantId,
                userId = user.id,
                courseId = id,
            )
        )

        return ResponseEntity.ok(res.course)
    }
}