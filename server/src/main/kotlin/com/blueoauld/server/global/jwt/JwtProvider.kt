package com.blueoauld.server.global.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class JwtProvider(

    @Value($$"${jwt.secret}") secret: String,
    @Value($$"${jwt.access-token-expiration}") val accessTokenExpiration: Long,
    @Value($$"${jwt.refresh-token-expiration}") val refreshTokenExpiration: Long,
) {

    private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun generateAccessToken(memberId: Long): String = generateToken(memberId, accessTokenExpiration)

    fun generateRefreshToken(memberId: Long): String = generateToken(memberId, refreshTokenExpiration)

    fun getMemberId(token: String): Long {
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
        return claims.subject.toLong()
    }

    private fun generateToken(memberId: Long, expirationMillis: Long): String {
        val now = Date()
        return Jwts.builder()
            .subject(memberId.toString())
            .issuedAt(now)
            .expiration(Date(now.time + expirationMillis))
            .signWith(key)
            .compact()
    }
}
