package com.richardamare.classroombackend.identity

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findAllByEmailAndRoleAndTenantId(email: String, role: UserRole, tenantId: String?): List<User>
    fun findByEmailAndRoleAndTenantId(email: String, role: UserRole, tenantId: String?): Optional<User>
}