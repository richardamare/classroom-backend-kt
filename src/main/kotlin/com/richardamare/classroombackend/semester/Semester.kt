package com.richardamare.classroombackend.semester

import com.richardamare.classroombackend.core.persistence.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("semesters")
class Semester(
    var name: String,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime,
    var tenantId: ObjectId,
) : BaseDocument()