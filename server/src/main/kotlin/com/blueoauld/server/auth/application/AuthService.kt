package com.blueoauld.server.auth.application

import com.blueoauld.server.auth.application.port.SmsSender
import com.blueoauld.server.auth.application.request.LoginRequest
import com.blueoauld.server.auth.application.request.ReissueRequest
import com.blueoauld.server.auth.application.request.SendVerificationCodeRequest
import com.blueoauld.server.auth.application.request.SignupRequest
import com.blueoauld.server.auth.application.response.TokenResponse
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.jwt.JwtProvider
import com.blueoauld.server.global.security.CodeGenerator
import com.blueoauld.server.global.security.PhoneHasher
import com.blueoauld.server.member.entity.Member
import com.blueoauld.server.member.repository.MemberRepository
import com.blueoauld.server.suspension.application.SuspensionService
import io.jsonwebtoken.JwtException
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId

@Service
class AuthService(

    private val memberRepository: MemberRepository,
    private val stringRedisTemplate: StringRedisTemplate,
    private val smsSender: SmsSender,
    private val codeGenerator: CodeGenerator,
    private val passwordEncoder: PasswordEncoder,
    private val phoneHasher: PhoneHasher,
    private val jwtProvider: JwtProvider,
    private val suspensionService: SuspensionService,
) {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")

        private const val REFRESH_TOKEN_KEY_PREFIX = "auth:refresh_token:"

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
        checkSuspended(request.phone)
        checkDailyLimit(request.phone)

        val verificationCode = codeGenerator.numeric(VERIFICATION_CODE_LENGTH)
        stringRedisTemplate.opsForValue().set(
            verificationCodeKey(request.phone),
            verificationCode,
            VERIFICATION_CODE_TTL
        )

        smsSender.send(request.phone, "[까치] 인증 번호는 [$verificationCode] 입니다.")
    }

    @Transactional
    fun signup(request: SignupRequest): TokenResponse {
        checkSuspended(request.phone)
        validateVerificationCode(request.phone, request.verificationCode)

        if (request.password != request.passwordConfirm) {
            throw BusinessException(ErrorCode.PASSWORD_CONFIRM_MISMATCH)
        }
        if (memberRepository.existsByPhone(request.phone)) {
            throw BusinessException(ErrorCode.ALREADY_REGISTERED_PHONE)
        }

        val member = Member(
            phone = request.phone,
            password = passwordEncoder.encode(request.password)!!,
            gender = request.gender,
        )
        val savedMember = memberRepository.save(member)

        stringRedisTemplate.delete(verificationCodeKey(request.phone))

        return issueTokens(savedMember.id)
    }

    fun login(request: LoginRequest): TokenResponse {
        val member = memberRepository.findByPhone(request.phone) ?: throw BusinessException(ErrorCode.LOGIN_FAILED)

        if (!member.matchesPassword(request.password, passwordEncoder)) {
            throw BusinessException(ErrorCode.LOGIN_FAILED)
        }

        return issueTokens(member.id)
    }

    fun reissue(request: ReissueRequest): TokenResponse {
        val memberId = try {
            jwtProvider.getMemberId(request.refreshToken)
        } catch (_: JwtException) {
            throw BusinessException(ErrorCode.INVALID_REFRESH_TOKEN)
        }

        val storedRefreshToken = stringRedisTemplate.opsForValue().get(refreshTokenKey(memberId))
        if (storedRefreshToken != request.refreshToken) {
            throw BusinessException(ErrorCode.INVALID_REFRESH_TOKEN)
        }

        return issueTokens(memberId)
    }

    fun logout(memberId: Long) {
        stringRedisTemplate.delete(refreshTokenKey(memberId))
    }

    private fun issueTokens(memberId: Long): TokenResponse {
        val accessToken = jwtProvider.generateAccessToken(memberId)
        val refreshToken = jwtProvider.generateRefreshToken(memberId)

        stringRedisTemplate.opsForValue().set(
            refreshTokenKey(memberId),
            refreshToken,
            Duration.ofMillis(jwtProvider.refreshTokenExpiration),
        )
        return TokenResponse(accessToken, refreshToken)
    }

    private fun refreshTokenKey(memberId: Long) = "$REFRESH_TOKEN_KEY_PREFIX$memberId"

    private fun validateVerificationCode(phone: String, verificationCode: String) {
        val savedCode = stringRedisTemplate.opsForValue().get(verificationCodeKey(phone))
            ?: throw BusinessException(ErrorCode.VERIFICATION_CODE_NOT_FOUND)

        if (savedCode != verificationCode) {
            throw BusinessException(ErrorCode.VERIFICATION_CODE_MISMATCH)
        }
    }

    private fun verificationCodeKey(phone: String) = "$VERIFICATION_CODE_KEY_PREFIX${phoneHasher.hash(phone)}"

    private fun checkSuspended(phone: String) {
        if (suspensionService.isSuspended(phone)) {
            throw BusinessException(ErrorCode.ACCOUNT_SUSPENDED)
        }
    }

    private fun checkDailyLimit(phone: String) {
        val key = "$DAILY_SEND_COUNT_KEY_PREFIX${LocalDate.now(KST)}:${phoneHasher.hash(phone)}"
        val count = stringRedisTemplate.execute(
            DAILY_SEND_COUNT_SCRIPT,
            listOf(key),
            DAILY_SEND_COUNT_TTL.seconds.toString(),
        ) ?: 1L

        if (count > MAX_DAILY_SEND_COUNT) {
            throw BusinessException(ErrorCode.DAILY_SEND_COUNT_EXCEEDED)
        }
    }
}
