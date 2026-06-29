package com.blueoauld.server.suspension.presentation

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.jwt.JwtAuthenticationFilter
import com.blueoauld.server.member.repository.MemberRepository
import com.blueoauld.server.suspension.application.SuspensionService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class SuspensionInterceptor(

    private val suspensionService: SuspensionService,
    private val memberRepository: MemberRepository,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val memberId = request.getAttribute(JwtAuthenticationFilter.MEMBER_ID_ATTRIBUTE) as? Long ?: return true
        val member = memberRepository.findByIdOrNull(memberId) ?: return true

        if (suspensionService.isSuspended(member.phone)) {
            throw BusinessException(ErrorCode.ACCOUNT_SUSPENDED)
        }
        return true
    }
}
