package com.richardamare.classroombackend.semester

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SemesterRepository : MongoRepository<Semester, ObjectId> {
    fun existsByNameAndTenantId(name: String, tenantId: ObjectId): Boolean
    fun findAllByTenantId(tenantId: ObjectId): List<Semester>
    fun findByIdAndTenantId(id: ObjectId, tenantId: ObjectId): Semester?
}