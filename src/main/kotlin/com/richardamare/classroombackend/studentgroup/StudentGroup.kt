package com.richardamare.classroombackend.studentgroup

import com.richardamare.classroombackend.core.persistence.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("student_groups")
class StudentGroup(
    var name: String,
    var tenantId: ObjectId,
    var semesterId: ObjectId,
) : BaseDocument()