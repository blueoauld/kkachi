package com.blueoauld.server.suspension.application

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.member.repository.MemberRepository
import com.blueoauld.server.suspension.application.response.SuspensionResponse
import com.blueoauld.server.suspension.entity.Suspension
import com.blueoauld.server.suspension.repository.SuspensionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

@Service
class SuspensionService(

    private val suspensionRepository: SuspensionRepository,
    private val memberRepository: MemberRepository,
) {

    companion object {
        private const val MINUTES_PER_DAY = 60.0 * 24
    }

    @Transactional
    fun suspend(phone: String, reason: String, days: Long) {
        if (days < 1) {
            throw BusinessException(ErrorCode.INVALID_SUSPENSION_DAYS)
        }

        suspensionRepository.save(
            Suspension(
                phone = phone,
                reason = reason,
                suspendedUntil = Instant.now().plus(days, ChronoUnit.DAYS),
            ),
        )
    }

    @Transactional(readOnly = true)
    fun isSuspended(phone: String): Boolean =
        suspensionRepository.existsByPhoneAndSuspendedUntilAfter(phone, Instant.now())

    @Transactional(readOnly = true)
    fun getMySuspension(memberId: Long): SuspensionResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        val now = Instant.now()
        val suspension = suspensionRepository
            .findFirstByPhoneAndSuspendedUntilAfterOrderBySuspendedUntilDesc(member.phone, now)
            ?: throw BusinessException(ErrorCode.SUSPENSION_NOT_FOUND)

        val remainingDays =
            ceil(Duration.between(now, suspension.suspendedUntil).toMinutes() / MINUTES_PER_DAY).toLong()

        return SuspensionResponse(
            reason = suspension.reason,
            remainingDays = remainingDays,
        )
    }
}
