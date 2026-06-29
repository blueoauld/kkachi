package com.blueoauld.server.global.config

import com.blueoauld.server.global.resolver.LoginMemberArgumentResolver
import com.blueoauld.server.suspension.presentation.SuspensionInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(

    private val loginMemberArgumentResolver: LoginMemberArgumentResolver,
    private val suspensionInterceptor: SuspensionInterceptor,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginMemberArgumentResolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(suspensionInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/v1/suspensions/me")
    }
}
