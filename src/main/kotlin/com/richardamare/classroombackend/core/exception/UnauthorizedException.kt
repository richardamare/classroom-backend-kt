package com.richardamare.classroombackend.core.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = UNAUTHORIZED)
class UnauthorizedException(message: String = "Unauthorized") : RuntimeException(message)