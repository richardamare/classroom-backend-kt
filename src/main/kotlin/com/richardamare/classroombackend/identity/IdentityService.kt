package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.identity.params.*
import com.richardamare.classroombackend.identity.result.LoginResult

interface IdentityService {
    fun createAdminUser(params: UserAdminCreateParams): String
    fun createOfficeUser(params: UserOfficeCreateParams): String
    fun login(params: LoginParams): LoginResult
    fun createTeacherUser(params: UserTeacherCreateParams): String
    fun createStudentUser(params: UserStudentCreateParams): String
}