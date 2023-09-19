package com.richardamare.classroombackend.core.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)