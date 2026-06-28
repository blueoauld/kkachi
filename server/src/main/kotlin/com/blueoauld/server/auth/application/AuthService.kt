package com.blueoauld.server.auth.application

import com.blueoauld.server.auth.application.port.SmsSender
import com.blueoauld.server.auth.application.request.SendVerificationCodeRequest
import com.blueoauld.server.global.security.CodeGenerator
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId

@Service
class AuthService(

    private val memberRepository: MemberRepository,
    private val stringRedisTemplate: StringRedisTemplate,
    private val smsSender: SmsSender,
    private val codeGenerator: CodeGenerator,
) {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")

        // 인증코드
        private const val VERIFICATION_CODE_LENGTH = 6
        private const val VERIFICATION_CODE_KEY_PREFIX = "auth:verification_code:"
        private val VERIFICATION_CODE_TTL = Duration.ofMinutes(5)

        // 하루 발송 횟수 제한
        private const val MAX_DAILY_SEND_COUNT = 3
        private const val DAILY_SEND_COUNT_KEY_PREFIX = "auth:verification_code:count:"
        private val DAILY_SEND_COUNT_TTL = Duration.ofDays(1)

        private val DAILY_SEND_COUNT_SCRIPT = RedisScript.of(
            """
            local count = redis.call('INCR', KEYS[1])
            if count == 1 then
                redis.call('EXPIRE', KEYS[1], ARGV[1])
            end
            return count
            """.trimIndent(),
            Long::class.java,
        )
    }

    fun sendVerificationCode(request: SendVerificationCodeRequest) {
        checkDailyLimit(request.phone)

        val verificationCode = codeGenerator.numeric(VERIFICATION_CODE_LENGTH)
        stringRedisTemplate.opsForValue().set(
            "$VERIFICATION_CODE_KEY_PREFIX${request.phone}",
            verificationCode,
            VERIFICATION_CODE_TTL
        )

        smsSender.send(request.phone, "[까치] 인증 번호는 [$verificationCode] 입니다.")
    }

    private fun checkDailyLimit(phone: String) {
        val key = "$DAILY_SEND_COUNT_KEY_PREFIX${LocalDate.now(KST)}:$phone"
        val count = stringRedisTemplate.execute(
            DAILY_SEND_COUNT_SCRIPT,
            listOf(key),
            DAILY_SEND_COUNT_TTL.seconds.toString(),
        ) ?: 1L

        require(count <= MAX_DAILY_SEND_COUNT) {
            "인증 번호 발송 횟수를 초과했습니다."
        }
    }
}
