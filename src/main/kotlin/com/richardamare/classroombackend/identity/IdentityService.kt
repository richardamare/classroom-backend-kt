package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.identity.params.LoginParams
import com.richardamare.classroombackend.identity.params.UserAdminCreateParams
import com.richardamare.classroombackend.identity.params.UserOfficeCreateParams
import com.richardamare.classroombackend.identity.result.LoginResult

interface IdentityService {
    fun createAdminUser(params: UserAdminCreateParams): String
    fun createOfficeUser(params: UserOfficeCreateParams): String
    fun login(params: LoginParams): LoginResult
}