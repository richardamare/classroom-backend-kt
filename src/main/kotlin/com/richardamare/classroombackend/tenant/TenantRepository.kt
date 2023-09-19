package com.richardamare.classroombackend.tenant

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TenantRepository : MongoRepository<Tenant, String> {
    fun findBySlug(slug: String): Tenant?
}