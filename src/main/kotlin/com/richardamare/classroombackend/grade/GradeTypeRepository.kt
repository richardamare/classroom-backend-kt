package com.richardamare.classroombackend.grade

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GradeTypeRepository : MongoRepository<GradeType, ObjectId> {
    fun findAllByTenantId(tenantId: ObjectId): List<GradeType>
}