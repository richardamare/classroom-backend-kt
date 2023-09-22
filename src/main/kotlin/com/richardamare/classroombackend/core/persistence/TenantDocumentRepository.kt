package com.richardamare.classroombackend.core.persistence

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TenantDocumentRepository<T : TenantDocument> : MongoRepository<T, ObjectId> {
    fun findByTenantId(tenantId: ObjectId): List<T>
    fun findByIdAndTenantId(id: ObjectId, tenantId: ObjectId): T?
}