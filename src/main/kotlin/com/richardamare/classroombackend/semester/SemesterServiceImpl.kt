package com.richardamare.classroombackend.semester

import com.richardamare.classroombackend.core.exception.ResourceNotFoundException
import com.richardamare.classroombackend.semester.dto.SemesterDTO
import com.richardamare.classroombackend.semester.params.SemesterCreateParams
import com.richardamare.classroombackend.semester.result.SemesterCreateResult
import com.richardamare.classroombackend.tenant.TenantRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime

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

    override fun listSemesters(tenantId: String): List<SemesterDTO> {
        val tenant = tenantRepository.findById(tenantId)
            .orElseThrow { ResourceNotFoundException("Tenant not found") }

        val results = semesterRepository.findAllByTenantId(tenant.id)
            .map {
                val now = LocalDateTime.now()
                val isCurrent = it.startDate.isBefore(now) && it.endDate.isAfter(now)

                SemesterDTO(
                    id = it.id.toHexString(),
                    name = it.name,
                    tenantId = it.tenantId.toHexString(),
                    startDate = it.startDate,
                    endDate = it.endDate,
                    isCurrent = isCurrent,
                )
            }.toMutableList()

        // if any of the semesters is not current, then the last one is current
        if (results.none { it.isCurrent }) {
            results.lastOrNull()?.let {
                results[results.lastIndex] = it.copy(isCurrent = true)
            }
        }

        return results
    }

    override fun getSemester(tenantId: String, id: String): SemesterDTO {
        val semester = semesterRepository.findByIdAndTenantId(ObjectId(id), ObjectId(tenantId))
            ?: throw ResourceNotFoundException("Semester not found")

        val now = LocalDateTime.now()

        val isCurrent = semester.startDate.isBefore(now) && semester.endDate.isAfter(now)

        return SemesterDTO(
            id = semester.id.toHexString(),
            name = semester.name,
            tenantId = semester.tenantId.toHexString(),
            startDate = semester.startDate,
            endDate = semester.endDate,
            isCurrent = isCurrent,
        )
    }
}