package com.richardamare.classroombackend.course

import com.richardamare.classroombackend.course.params.CourseCreateParams
import com.richardamare.classroombackend.course.params.CourseListParams
import com.richardamare.classroombackend.course.result.CourseCreateResult
import com.richardamare.classroombackend.course.result.CourseListResult

interface CourseService {
    fun createCourse(params: CourseCreateParams): CourseCreateResult
    fun listCourses(params: CourseListParams): CourseListResult
}