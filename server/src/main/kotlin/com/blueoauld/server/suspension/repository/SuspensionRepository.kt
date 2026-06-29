package com.blueoauld.server.suspension.repository

import com.blueoauld.server.suspension.entity.Suspension
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface SuspensionRepository : JpaRepository<Suspension, Long> {

    fun existsByPhoneAndSuspendedUntilAfter(phone: String, now: Instant): Boolean

    fun findFirstByPhoneAndSuspendedUntilAfterOrderBySuspendedUntilDesc(phone: String, now: Instant): Suspension?
}
