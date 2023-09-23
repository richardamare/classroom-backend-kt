package com.richardamare.classroombackend.auth

import com.richardamare.classroombackend.identity.User
import com.richardamare.classroombackend.identity.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthUser(
    private val user: User,
) : UserDetails {

    val role: UserRole
        get() = user.role

    val id: String
        get() = user.id.toHexString()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(
            SimpleGrantedAuthority("ROLE_${user.role.name.uppercase()}")
        )
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.id.toHexString()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return user.isVerified
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}