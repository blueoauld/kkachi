package com.blueoauld.server.point.repository

import com.blueoauld.server.point.entity.PointWallet
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PointWalletRepository : JpaRepository<PointWallet, Long> {

    fun findByMemberId(memberId: Long): PointWallet?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM PointWallet w WHERE w.memberId = :memberId")
    fun findByMemberIdForUpdate(@Param("memberId") memberId: Long): PointWallet?
}
