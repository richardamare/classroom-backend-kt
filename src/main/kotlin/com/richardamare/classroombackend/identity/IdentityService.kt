package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.identity.params.*
import com.richardamare.classroombackend.identity.result.LoginResult
import com.richardamare.classroombackend.identity.result.RegisterResult

interface IdentityService {
    fun login(params: LoginParams): LoginResult
    fun createAdminUser(params: UserAdminCreateParams): RegisterResult
    fun createOfficeUser(params: UserOfficeCreateParams): RegisterResult
    fun createTeacherUser(params: UserTeacherCreateParams): RegisterResult
    fun createStudentUser(params: UserStudentCreateParams): RegisterResult
    fun createParentUser(params: UserParentCreateParams): RegisterResult
}