package com.richardamare.classroombackend.core.persistence

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.Update
import java.time.ZonedDateTime

abstract class BaseDocument(
    @Id
    open var id: ObjectId = ObjectId.get(),
    open var createdAt: ZonedDateTime = ZonedDateTime.now(),
    open var updatedAt: ZonedDateTime = ZonedDateTime.now()
) {
    @Update
    fun onUpdate() {
        updatedAt = ZonedDateTime.now()
    }

    val _id: String
        get() = id.toHexString()
}