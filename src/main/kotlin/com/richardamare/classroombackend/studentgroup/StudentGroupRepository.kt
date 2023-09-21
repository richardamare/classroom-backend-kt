package com.richardamare.classroombackend.studentgroup

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentGroupRepository : MongoRepository<StudentGroup, String>