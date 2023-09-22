package com.richardamare.classroombackend.course

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : MongoRepository<Course, ObjectId> {
    fun findAllByTenantId(tenantId: ObjectId): List<Course>
    fun findByIdAndTenantId(id: ObjectId, tenantId: ObjectId): Course?
}