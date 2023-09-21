package com.richardamare.classroombackend.studentgroup

import com.richardamare.classroombackend.identity.UserRepository
import com.richardamare.classroombackend.semester.SemesterRepository
import com.richardamare.classroombackend.studentgroup.params.StudentGroupAddStudentParams
import com.richardamare.classroombackend.studentgroup.params.StudentGroupCreateParams
import com.richardamare.classroombackend.studentgroup.params.StudentGroupRemoveStudentParams
import com.richardamare.classroombackend.studentgroup.result.StudentGroupCreateResult
import com.richardamare.classroombackend.tenant.TenantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentGroupServiceImpl(
    private val studentGroupRepository: StudentGroupRepository,
    private val tenantRepository: TenantRepository,
    private val semesterRepository: SemesterRepository,
    private val userRepository: UserRepository,
) : StudentGroupService {

    private val logger = LoggerFactory.getLogger(StudentGroupServiceImpl::class.java)

    override fun createStudentGroup(params: StudentGroupCreateParams): StudentGroupCreateResult {
        val tenant = tenantRepository.findById(params.tenantId)
            .orElseThrow { IllegalArgumentException("Tenant not found") }

        val semester = semesterRepository.findById(params.semesterId)
            .orElseThrow { IllegalArgumentException("Semester not found") }

        return try {
            val group = studentGroupRepository.save(
                StudentGroup(
                    name = params.name,
                    tenantId = tenant.id,
                    semesterId = semester.id,
                    type = params.type,
                    studentIds = emptyList(),
                )
            )

            StudentGroupCreateResult(
                id = group.id.toHexString()
            )
        } catch (e: Exception) {
            logger.error("Error creating student group", e)
            throw IllegalStateException()
        }
    }

    override fun addStudentToGroup(params: StudentGroupAddStudentParams) {

        val tenant = tenantRepository.findById(params.tenantId)
            .orElseThrow { IllegalArgumentException("Tenant not found") }

        val group = studentGroupRepository.findById(params.studentGroupId)
            .orElseThrow { IllegalArgumentException("Group not found") }

        if (group.tenantId != tenant.id)
            throw IllegalArgumentException("Group not found")

        val student = userRepository.findById(params.studentId)
            .orElseThrow { IllegalArgumentException("Student not found") }

        if (student.tenantId != tenant.id)
            throw IllegalArgumentException("Student not found")

        group.studentIds += student.id

        try {
            studentGroupRepository.save(group)
        } catch (e: Exception) {
            logger.error("Error updating student group", e)
            throw IllegalStateException()
        }
    }

    override fun removeStudentFromGroup(params: StudentGroupRemoveStudentParams) {
        val tenant = tenantRepository.findById(params.tenantId)
            .orElseThrow { IllegalArgumentException("Tenant not found") }

        val group = studentGroupRepository.findById(params.studentGroupId)
            .orElseThrow { IllegalArgumentException("Group not found") }

        if (group.tenantId != tenant.id)
            throw IllegalArgumentException("Group not found")

        val student = userRepository.findById(params.studentId)
            .orElseThrow { IllegalArgumentException("Student not found") }

        if (student.tenantId != tenant.id)
            throw IllegalArgumentException("Student not found")

        group.studentIds -= student.id

        try {
            studentGroupRepository.save(group)
        } catch (e: Exception) {
            logger.error("Error updating student group", e)
            throw IllegalStateException()
        }
    }
}