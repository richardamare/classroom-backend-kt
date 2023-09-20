package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.core.persistence.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User(
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var tenantId: ObjectId?,
    var role: UserRole,
    var isVerified: Boolean = false,
) : BaseDocument()