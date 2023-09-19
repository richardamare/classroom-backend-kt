package com.richardamare.classroombackend.tenant

import com.richardamare.classroombackend.core.persistence.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tenants")
class Tenant(
    var name: String,
    var slug: String,
    var ownerId: ObjectId,
) : BaseDocument()