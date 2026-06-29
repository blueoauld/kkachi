package com.blueoauld.server.point.entity

import com.blueoauld.server.point.entity.type.PointTransactionType
import jakarta.persistence.*
import java.time.Instant

@Entity
class PointHistory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "member_id", nullable = false)
    val memberId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: PointTransactionType,

    @Column(name = "amount", nullable = false)
    val amount: Long,

    @Column(name = "balance_after", nullable = false)
    val balanceAfter: Long,

    @Column(name = "description", length = 200, nullable = false)
    val description: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
