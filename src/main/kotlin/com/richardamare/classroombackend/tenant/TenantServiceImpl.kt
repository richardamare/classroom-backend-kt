package com.richardamare.classroombackend.tenant

import com.richardamare.classroombackend.identity.UserRepository
import com.richardamare.classroombackend.tenant.dto.TenantDTO
import com.richardamare.classroombackend.tenant.params.TenantCreateParams
import com.richardamare.classroombackend.tenant.params.TenantListParams
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TenantServiceImpl(
    private val tenantRepository: TenantRepository,
    private val userRepository: UserRepository,
) : TenantService {

    private val logger = LoggerFactory.getLogger(TenantServiceImpl::class.java)

    override fun createTenant(params: TenantCreateParams): String {

        val owner = userRepository.findById(params.ownerId).orElseThrow {
            logger.error("Error finding owner with id ${params.ownerId}")
            // ownerId is provided from the JWT token, so this should never happen
            IllegalStateException()
        }

        val existingTenant = tenantRepository.findBySlug(params.slug)
        if (existingTenant != null) {
            throw IllegalArgumentException("Tenant with slug ${params.slug} already exists")
        }

        val tenant = tenantRepository.save(
            Tenant(
                name = params.name,
                slug = params.slug,
                ownerId = owner.id,
            )
        )

        // send event to billing service to create Stripe customer

        return tenant.id.toHexString()
    }

    override fun listTenants(params: TenantListParams): List<TenantDTO> {
        val ownerId = ObjectId(params.ownerId)
        val tenants = tenantRepository.findAllByOwnerId(ownerId)

        return tenants.map {
            TenantDTO(
                id = it.id.toHexString(),
                name = it.name,
                slug = it.slug,
            )
        }
    }
}