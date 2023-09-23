package com.richardamare.classroombackend.tenant

import com.richardamare.classroombackend.event.AppEvent
import com.richardamare.classroombackend.identity.User
import com.richardamare.classroombackend.identity.UserRepository
import com.richardamare.classroombackend.identity.UserRole
import com.richardamare.classroombackend.tenant.params.TenantCreateParams
import com.richardamare.classroombackend.tenant.params.TenantListParams
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationEventPublisher
import java.util.*

@ExtendWith(MockitoExtension::class)
class TenantServiceImplTest {
    @Mock
    lateinit var tenantRepository: TenantRepository

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var publisher: ApplicationEventPublisher

    @InjectMocks
    lateinit var underTest: TenantServiceImpl

    lateinit var mockTenant: Tenant

    lateinit var mockUser: User

    @BeforeEach
    fun setUp() {
        mockTenant = Tenant(
            name = "Test Tenant",
            slug = "test-tenant",
            ownerId = ObjectId("5f9a2b9b9d6b9b1d3c9d9d9d"),
        )

        mockUser = User(
            email = "johndoe@example.com",
            password = "password",
            firstName = "John",
            lastName = "Doe",
            tenantId = null,
            isVerified = true,
            role = UserRole.ADMIN,
        )
    }

    @Test
    fun shouldCreateTenant() {
        // given
        val params = TenantCreateParams(
            slug = "test-tenant",
            name = "Test Tenant",
            ownerId = "5f9a2b9b9d6b9b1d3c9d9d9d",
        )

        // when
        `when`(userRepository.findById(any(ObjectId::class.java)))
            .thenReturn(Optional.of(mockUser))
        `when`(tenantRepository.findBySlug(params.slug))
            .thenReturn(null)
        `when`(tenantRepository.save(any(Tenant::class.java)))
            .thenReturn(mockTenant)

        underTest.createTenant(params)

        // then
        verify(userRepository).findById(ObjectId(params.ownerId))
        verify(tenantRepository).findBySlug(params.slug)
        verify(tenantRepository).save(any(Tenant::class.java))
        verify(publisher).publishEvent(any(AppEvent.TenantCreated::class.java))
    }

    @Test
    fun shouldNotCreateTenantIfOwnerNotFound() {
        // given
        val params = TenantCreateParams(
            slug = "test-tenant",
            name = "Test Tenant",
            ownerId = "5f9a2b9b9d6b9b1d3c9d9d9d",
        )

        // when
        `when`(userRepository.findById(any(ObjectId::class.java)))
            .thenReturn(Optional.empty())

        // then

        assertThrows<IllegalStateException> {
            underTest.createTenant(params)
        }

        verify(userRepository).findById(ObjectId(params.ownerId))
    }

    @Test
    fun shouldNotCreateTenantIfAlreadyExists() {
        // given
        val params = TenantCreateParams(
            slug = "test-tenant",
            name = "Test Tenant",
            ownerId = "5f9a2b9b9d6b9b1d3c9d9d9d",
        )

        // when
        `when`(userRepository.findById(any(ObjectId::class.java)))
            .thenReturn(Optional.of(mockUser))
        `when`(tenantRepository.findBySlug(params.slug))
            .thenReturn(mockTenant)

        // then
        assertThrows<IllegalArgumentException> {
            underTest.createTenant(params)
        }

        verify(userRepository).findById(ObjectId(params.ownerId))
        verify(tenantRepository).findBySlug(params.slug)
    }

    @Test
    fun shouldListTenants() {
        // given
        val params = TenantListParams(
            ownerId = "5f9a2b9b9d6b9b1d3c9d9d9d",
        )

        // when
        `when`(tenantRepository.findAllByOwnerId(ObjectId(params.ownerId)))
            .thenReturn(listOf(mockTenant))

        underTest.listTenants(params)

        // then
        verify(tenantRepository).findAllByOwnerId(ObjectId(params.ownerId))
    }
}