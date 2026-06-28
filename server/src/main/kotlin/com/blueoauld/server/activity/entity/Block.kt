package com.blueoauld.server.activity.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "block",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["blocker_id", "blocked_id"]),
    ],
)
class Block(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "blocker_id", nullable = false)
    val blockerId: Long,

    @Column(name = "blocked_id", nullable = false)
    val blockedId: Long,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
