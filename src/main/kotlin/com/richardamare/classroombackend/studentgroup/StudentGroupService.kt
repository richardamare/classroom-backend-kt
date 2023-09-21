package com.richardamare.classroombackend.studentgroup

import com.richardamare.classroombackend.studentgroup.params.StudentGroupAddStudentParams
import com.richardamare.classroombackend.studentgroup.params.StudentGroupCreateParams
import com.richardamare.classroombackend.studentgroup.params.StudentGroupRemoveStudentParams
import com.richardamare.classroombackend.studentgroup.result.StudentGroupCreateResult

interface StudentGroupService {
    fun createStudentGroup(params: StudentGroupCreateParams): StudentGroupCreateResult
    fun addStudentToGroup(params: StudentGroupAddStudentParams)
    fun removeStudentFromGroup(params: StudentGroupRemoveStudentParams)
}