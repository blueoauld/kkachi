package com.blueoauld.server.point.entity

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import jakarta.persistence.*
import java.time.Instant

@Entity
class PointWallet(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "member_id", unique = true, nullable = false)
    val memberId: Long,

    @Column(name = "balance", nullable = false)
    var balance: Long = 0,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
) {

    fun earn(amount: Long): Long {
        this.balance += amount
        return this.balance
    }

    fun spend(amount: Long): Long {
        if (this.balance < amount) {
            throw BusinessException(ErrorCode.INSUFFICIENT_POINT)
        }

        this.balance -= amount
        return this.balance
    }

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
        this.updatedAt = Instant.now()
    }

    @PreUpdate
    fun onUpdate() {
        this.updatedAt = Instant.now()
    }
}
