package com.richardamare.classroombackend.course

import com.richardamare.classroombackend.course.params.CourseCreateParams
import com.richardamare.classroombackend.course.result.CourseCreateResult

interface CourseService {
    fun createCourse(params: CourseCreateParams): CourseCreateResult
}