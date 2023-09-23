package com.richardamare.classroombackend.course.result

import com.richardamare.classroombackend.course.dto.CourseListDTO

data class CourseListResult(
    val courses: List<CourseListDTO>
)
