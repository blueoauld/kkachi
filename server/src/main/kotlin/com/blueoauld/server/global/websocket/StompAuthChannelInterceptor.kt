package com.blueoauld.server.global.websocket

import com.blueoauld.server.global.jwt.JwtProvider
import org.springframework.http.HttpHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageDeliveryException
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

@Component
class StompAuthChannelInterceptor(

    private val jwtProvider: JwtProvider,
) : ChannelInterceptor {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

        if (accessor != null && StompCommand.CONNECT == accessor.command) {
            val token = resolveToken(accessor) ?: throw MessageDeliveryException("인증 토큰이 필요합니다.")
            val memberId = try {
                jwtProvider.getMemberId(token)
            } catch (_: Exception) {
                throw MessageDeliveryException("유효하지 않은 토큰입니다.")
            }
            accessor.user = StompPrincipal(memberId)
        }

        return message
    }

    private fun resolveToken(accessor: StompHeaderAccessor): String? {
        val header = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return if (header.startsWith(BEARER_PREFIX)) header.substring(BEARER_PREFIX.length) else null
    }
}
