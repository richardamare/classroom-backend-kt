package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.identity.params.LoginParams
import com.richardamare.classroombackend.identity.params.UserCreateParams
import com.richardamare.classroombackend.identity.result.LoginResult

interface IdentityService {
    fun createUser(params: UserCreateParams): String
    fun login(params: LoginParams): LoginResult
}