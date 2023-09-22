package com.richardamare.classroombackend.tenant

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TenantRepository : MongoRepository<Tenant, ObjectId> {
    fun findBySlug(slug: String): Tenant?
    fun findAllByOwnerId(ownerId: ObjectId): List<Tenant>
}