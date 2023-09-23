package com.richardamare.classroombackend.core.exception

import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = FORBIDDEN)
class ForbiddenException(message: String = "Forbidden") : HttpException(message) {
    override val statusCode: Int
        get() = FORBIDDEN.value()
}