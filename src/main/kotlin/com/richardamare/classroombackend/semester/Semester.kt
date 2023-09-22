package com.richardamare.classroombackend.semester

import com.richardamare.classroombackend.core.persistence.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document("semesters")
class Semester(
    var name: String,
    var startDate: ZonedDateTime,
    var endDate: ZonedDateTime,
    var tenantId: ObjectId,
) : BaseDocument()