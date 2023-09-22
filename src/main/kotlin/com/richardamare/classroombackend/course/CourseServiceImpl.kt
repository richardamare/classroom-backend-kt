package com.richardamare.classroombackend.course

import com.richardamare.classroombackend.course.params.CourseCreateParams
import com.richardamare.classroombackend.course.result.CourseCreateResult
import com.richardamare.classroombackend.identity.UserRepository
import com.richardamare.classroombackend.semester.SemesterRepository
import com.richardamare.classroombackend.studentgroup.StudentGroupRepository
import com.richardamare.classroombackend.tenant.TenantRepository
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CourseServiceImpl(
    private val courseRepository: CourseRepository,
    private val tenantRepository: TenantRepository,
    private val userRepository: UserRepository,
    private val semesterRepository: SemesterRepository,
    private val studentGroupRepository: StudentGroupRepository,
) : CourseService {

    private val logger = LoggerFactory.getLogger(CourseServiceImpl::class.java)

    override fun createCourse(params: CourseCreateParams): CourseCreateResult {

        val tenant = tenantRepository.findById(ObjectId(params.tenantId))
            .orElseThrow { IllegalArgumentException("Tenant not found") }

        val semester = semesterRepository.findById(ObjectId(params.semesterId))
            .orElseThrow { IllegalArgumentException("Semester not found") }

        val teacher = userRepository.findById(ObjectId(params.teacherId))
            .orElseThrow { IllegalArgumentException("Teacher not found") }

        val studentGroup = studentGroupRepository.findById(ObjectId(params.studentGroupId))
            .orElseThrow { IllegalArgumentException("Student group not found") }

        return try {
            val course = courseRepository.save(
                Course(
                    name = params.name,
                    description = params.description,
                    tenantId = tenant.id,
                    semesterId = semester.id,
                    type = params.type,
                    studentGroupId = studentGroup.id,
                    teacherId = teacher.id,
                )
            )

            CourseCreateResult(
                id = course.id.toString()
            )
        } catch (e: Exception) {
            logger.error("Error creating course", e)
            throw IllegalArgumentException()
        }
    }
}