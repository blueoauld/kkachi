package com.blueoauld.server.global.resolver

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.jwt.JwtAuthenticationFilter
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginMemberArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(LoginMember::class.java) && parameter.parameterType == Long::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)

        (request?.getAttribute(JwtAuthenticationFilter.MEMBER_ID_ATTRIBUTE) as? Long)?.let { return it }

        if (request?.getAttribute(JwtAuthenticationFilter.TOKEN_EXPIRED_ATTRIBUTE) == true) {
            throw BusinessException(ErrorCode.EXPIRED_TOKEN)
        }
        throw BusinessException(ErrorCode.UNAUTHORIZED)
    }
}
