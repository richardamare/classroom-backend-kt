package com.richardamare.classroombackend.grade

import com.richardamare.classroombackend.core.persistence.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("grade_types")
class GradeType(
    var name: String,
    var description: String,
    var tenantId: ObjectId,
) : BaseDocument()