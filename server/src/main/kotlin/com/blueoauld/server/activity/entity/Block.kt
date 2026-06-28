package com.blueoauld.server.activity.entity

import com.blueoauld.server.member.entity.Member
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id", nullable = false)
    val blocker: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id", nullable = false)
    val blocked: Member,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
