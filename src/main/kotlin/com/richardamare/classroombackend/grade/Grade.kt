package com.richardamare.classroombackend.grade

import com.richardamare.classroombackend.core.persistence.BaseDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("grades")
class Grade(
    var name: String,
    var tenantId: ObjectId,
    var courseId: ObjectId,
    var maxGrade: Double,
    var gradeDate: LocalDateTime,
    var gradeTypeId: ObjectId,
    var grades: List<StudentGrade>,
) : BaseDocument() {
    class StudentGrade(
        var studentId: ObjectId,
        var note: String,
        var grade: Double,
    )
}