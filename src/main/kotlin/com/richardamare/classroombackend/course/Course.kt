package com.richardamare.classroombackend.course

import com.richardamare.classroombackend.core.persistence.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("courses")
class Course(
    var name: String,
    var description: String,
    var tenantId: ObjectId,
    var semesterId: ObjectId,
    var type: CourseType,
    var studentGroupId: ObjectId,
    var teacherId: ObjectId,
) : BaseDocument()