package com.blueoauld.server.global.jwt

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
            } catch (_: JwtException) {
                // 유효하지 않은 토큰 -> 그냥 통과
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return if (header.startsWith(BEARER_PREFIX)) header.substring(BEARER_PREFIX.length) else null
    }
}
