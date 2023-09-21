package com.richardamare.classroombackend.studentgroup

import com.richardamare.classroombackend.semester.SemesterRepository
import com.richardamare.classroombackend.studentgroup.params.StudentGroupCreateParams
import com.richardamare.classroombackend.studentgroup.result.StudentGroupCreateResult
import com.richardamare.classroombackend.tenant.TenantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentGroupServiceImpl(
    private val studentGroupRepository: StudentGroupRepository,
    private val tenantRepository: TenantRepository,
    private val semesterRepository: SemesterRepository,
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
}