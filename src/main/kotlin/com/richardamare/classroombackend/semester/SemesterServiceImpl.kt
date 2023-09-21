package com.richardamare.classroombackend.semester

import com.richardamare.classroombackend.core.exception.ResourceNotFoundException
import com.richardamare.classroombackend.semester.params.SemesterCreateParams
import com.richardamare.classroombackend.semester.result.SemesterCreateResult
import com.richardamare.classroombackend.tenant.TenantRepository
import org.springframework.stereotype.Service

@Service
class SemesterServiceImpl(
    private val semesterRepository: SemesterRepository,
    private val tenantRepository: TenantRepository,
) : SemesterService {

    override fun createSemester(params: SemesterCreateParams): SemesterCreateResult {

        val tenant = tenantRepository.findById(params.tenantId)
            .orElseThrow { ResourceNotFoundException("Tenant not found") }

        semesterRepository.existsByNameAndTenantId(params.name, tenant.id)
            .takeIf { it }
            ?.let { throw IllegalArgumentException("Semester already exists") }

        if (params.startDate.isAfter(params.endDate)) {
            throw IllegalArgumentException("Start date must be before end date")
        }

        val semester = semesterRepository.save(
            Semester(
                name = params.name,
                startDate = params.startDate,
                endDate = params.endDate,
                tenantId = tenant.id,
            )
        )

        return SemesterCreateResult(
            id = semester.id.toHexString()
        )
    }
}