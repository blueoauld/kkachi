package com.blueoauld.server.suspension.repository

import com.blueoauld.server.suspension.entity.Suspension
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface SuspensionRepository : JpaRepository<Suspension, Long> {

    fun existsByPhoneAndSuspendedUntilAfterAndReleasedAtIsNull(phone: String, now: Instant): Boolean

    fun findFirstByPhoneAndSuspendedUntilAfterAndReleasedAtIsNullOrderBySuspendedUntilDesc(
        phone: String,
        now: Instant,
    ): Suspension?

    fun findByPhoneAndSuspendedUntilAfterAndReleasedAtIsNull(phone: String, now: Instant): List<Suspension>

    fun countByPhone(phone: String): Long
}
