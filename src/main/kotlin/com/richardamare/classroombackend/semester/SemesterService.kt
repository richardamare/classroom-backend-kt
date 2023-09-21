package com.richardamare.classroombackend.semester

import com.richardamare.classroombackend.semester.params.SemesterCreateParams
import com.richardamare.classroombackend.semester.result.SemesterCreateResult

interface SemesterService {
    fun createSemester(params: SemesterCreateParams): SemesterCreateResult
}