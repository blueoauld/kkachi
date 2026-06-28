package com.blueoauld.server.global.security

import org.springframework.stereotype.Component
import java.security.SecureRandom
import kotlin.math.pow

@Component
class SecureRandomCodeGenerator : CodeGenerator {

    private val secureRandom = SecureRandom()

    override fun numeric(length: Int): String {
        require(length in 1..9) { "길이는 1 ~ 9 사이여야 합니다." }

        val bound = 10.0.pow(length).toInt()
        return secureRandom.nextInt(bound).toString().padStart(length, '0')
    }
}
