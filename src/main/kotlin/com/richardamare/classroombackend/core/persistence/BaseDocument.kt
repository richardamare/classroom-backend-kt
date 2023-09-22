package com.richardamare.classroombackend.core.persistence

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.Update
import java.time.LocalDateTime

abstract class BaseDocument(
    @Id
    open var id: ObjectId = ObjectId.get(),
    open var createdAt: LocalDateTime = LocalDateTime.now(),
    open var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @Update
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }

    val _id: String
        get() = id.toHexString()
}