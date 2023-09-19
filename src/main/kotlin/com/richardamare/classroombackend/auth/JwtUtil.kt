package com.richardamare.classroombackend.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import kotlin.reflect.KFunction1

@Service
class JwtUtil(
    @Value("\${application.config.jwt.secret}") private val jwtSecret: String,
) {

    fun extractUsername(token: String?): String? {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String?, claimsResolver: KFunction1<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    fun generateToken(userDetails: UserDetails): String? {
        return generateToken(hashMapOf(), userDetails)
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String? {
        val issuedAt = Date(System.currentTimeMillis())
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(issuedAt)
            .setExpiration(Date(System.currentTimeMillis() + Companion.expirationTime))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username.equals(userDetails.username)) && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token)?.before(Date()) ?: false
    }

    fun extractExpiration(token: String?): Date? {
        return extractClaim(token, Claims::getExpiration)
    }

    fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun getSignKey(): Key {
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    companion object {
        const val expirationTime = 1000 * 60 * 24 // default 24 hours
    }
}