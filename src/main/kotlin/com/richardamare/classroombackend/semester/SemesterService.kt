package com.richardamare.classroombackend.semester

import com.richardamare.classroombackend.semester.dto.SemesterDTO
import com.richardamare.classroombackend.semester.params.SemesterCreateParams
import com.richardamare.classroombackend.semester.result.SemesterCreateResult

interface SemesterService {
    fun createSemester(params: SemesterCreateParams): SemesterCreateResult
    fun listSemesters(tenantId: String): List<SemesterDTO>
    fun getSemester(tenantId: String, id: String): SemesterDTO
}