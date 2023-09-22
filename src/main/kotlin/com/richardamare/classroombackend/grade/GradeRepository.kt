package com.richardamare.classroombackend.grade

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GradeRepository : MongoRepository<Grade, ObjectId> {
    fun findAllByTenantId(tenantId: ObjectId): List<Grade>
}