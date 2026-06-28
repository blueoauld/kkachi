package com.blueoauld.server.global.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(

    private val jwtProvider: JwtProvider,
) : OncePerRequestFilter() {

    companion object {
        const val MEMBER_ID_ATTRIBUTE = "memberId"
        const val TOKEN_EXPIRED_ATTRIBUTE = "tokenExpired"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        resolveToken(request)?.let { token ->
            try {
                request.setAttribute(MEMBER_ID_ATTRIBUTE, jwtProvider.getMemberId(token))
            } catch (_: ExpiredJwtException) {
                request.setAttribute(TOKEN_EXPIRED_ATTRIBUTE, true)
            } catch (_: JwtException) {
                // 무효 토큰(서명 불일치/형식 오류 등등) -> 표시 없이 통과
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return if (header.startsWith(BEARER_PREFIX)) header.substring(BEARER_PREFIX.length) else null
    }
}
