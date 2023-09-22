package com.richardamare.classroombackend.core.persistence

import org.bson.types.ObjectId

abstract class TenantDocument : BaseDocument() {
    abstract var tenantId: ObjectId
}