package com.blueoauld.server.global.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class PhoneHasher(

    @Value("\${app.crypto.secret-key}") secretKey: String,
) {

    companion object {
        private const val ALGORITHM = "HmacSHA256"
    }

    private val key = SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), ALGORITHM)

    fun hash(phone: String): String {
        val mac = Mac.getInstance(ALGORITHM)
        mac.init(key)
        return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(mac.doFinal(phone.toByteArray(StandardCharsets.UTF_8)))
    }
}
