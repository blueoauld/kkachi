package com.blueoauld.server.activity.entity

import com.blueoauld.server.member.entity.Member
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "heart",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["sender_id", "receiver_id"]),
    ],
)
class Heart(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    val sender: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    val receiver: Member,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
