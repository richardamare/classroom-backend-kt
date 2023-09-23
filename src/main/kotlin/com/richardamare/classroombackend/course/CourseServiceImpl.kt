package com.richardamare.classroombackend.course

import com.richardamare.classroombackend.course.dto.CourseListDTO
import com.richardamare.classroombackend.course.params.CourseCreateParams
import com.richardamare.classroombackend.course.params.CourseListParams
import com.richardamare.classroombackend.course.result.CourseCreateResult
import com.richardamare.classroombackend.course.result.CourseListResult
import com.richardamare.classroombackend.identity.UserRepository
import com.richardamare.classroombackend.identity.UserRole
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

    private fun listStudentCourses(params: CourseListParams): CourseListResult {

        val user = userRepository.findById(ObjectId(params.userId))
            .orElseThrow { IllegalArgumentException("User not found") }

        if (user.role != UserRole.STUDENT)
            throw IllegalArgumentException("User is not a student")

        val studentGroups = studentGroupRepository.findAllByStudentIdsContaining(user.id)

        val courses = courseRepository.findAllByTenantId(ObjectId(params.tenantId))
            .filter { c -> studentGroups.any { g -> g.id == c.studentGroupId } }

        if (courses.isEmpty()) return CourseListResult(courses = emptyList())

        val teachers = userRepository.findAllById(
            courses.map { it.teacherId.toHexString() }.toSet().map { ObjectId(it) }
        )

        return CourseListResult(
            courses = courses.map { course ->

                val teacher = teachers.find { it.id == course.teacherId }
                    ?: throw IllegalArgumentException("Teacher not found")

                val group = studentGroups.find { it.id == course.studentGroupId }
                    ?: throw IllegalArgumentException("Student group not found")

                CourseListDTO(
                    id = course.id.toString(),
                    name = course.name,
                    description = course.description,
                    type = course.type,
                    teacher = CourseListDTO.Teacher(
                        id = teacher.id.toString(),
                        fullName = "${teacher.firstName} ${teacher.lastName}"
                    ),
                    group = CourseListDTO.Group(
                        id = group.id.toString(),
                        name = group.name
                    )
                )
            }
        )
    }

    private fun listTeacherCourses(params: CourseListParams): CourseListResult {

        val user = userRepository.findById(ObjectId(params.userId))
            .orElseThrow { IllegalArgumentException("User not found") }

        if (user.role != UserRole.TEACHER)
            throw IllegalArgumentException("User is not a teacher")

        val courses = courseRepository.findAllByTenantId(ObjectId(params.tenantId))
            .filter { it.teacherId == user.id }

        if (courses.isEmpty()) return CourseListResult(courses = emptyList())

        val studentGroups = studentGroupRepository.findAllById(
            courses.distinctBy { it.studentGroupId.toHexString() }.map { it.studentGroupId }
        )

        return CourseListResult(
            courses = courses.map { course ->

                val teacher = userRepository.findById(course.teacherId)
                    .orElseThrow { IllegalArgumentException("Teacher not found") }

                val group = studentGroups.find { it.id == course.studentGroupId }
                    ?: throw IllegalArgumentException("Student group not found")

                CourseListDTO(
                    id = course.id.toString(),
                    name = course.name,
                    description = course.description,
                    type = course.type,
                    teacher = CourseListDTO.Teacher(
                        id = teacher.id.toString(),
                        fullName = "${teacher.firstName} ${teacher.lastName}"
                    ),
                    group = CourseListDTO.Group(
                        id = group.id.toString(),
                        name = group.name
                    )
                )
            }
        )
    }

    override fun listCourses(params: CourseListParams): CourseListResult {
        val user = userRepository.findById(ObjectId(params.userId))
            .orElseThrow { IllegalArgumentException("User not found") }

        return when (user.role) {
            UserRole.STUDENT -> listStudentCourses(params)
            UserRole.TEACHER -> listTeacherCourses(params)
            else -> throw IllegalArgumentException("Invalid role")
        }
    }
}