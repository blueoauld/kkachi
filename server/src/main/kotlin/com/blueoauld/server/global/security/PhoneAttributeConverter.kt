package com.blueoauld.server.global.security

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
@Converter
class PhoneAttributeConverter(

    @Value("\${app.crypto.secret-key}") secretKey: String,
) : AttributeConverter<String, String> {

    companion object {
        private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    }

    private val key = SecretKeySpec(
        MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(StandardCharsets.UTF_8)),
        "AES",
    )
    private val iv = IvParameterSpec(ByteArray(16))

    override fun convertToDatabaseColumn(attribute: String?): String? {
        if (attribute == null) {
            return null
        }

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.toByteArray(StandardCharsets.UTF_8)))
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        if (dbData == null) {
            return null
        }

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        return String(cipher.doFinal(Base64.getDecoder().decode(dbData)), StandardCharsets.UTF_8)
    }
}
