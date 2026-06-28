package com.blueoauld.server.auth.infrastructure

import com.blueoauld.server.auth.application.port.SmsSender
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoggingSmsSender : SmsSender {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun send(phone: String, message: String) {
        log.info("[SMS] to={}, message={}", phone, message)
    }
}
