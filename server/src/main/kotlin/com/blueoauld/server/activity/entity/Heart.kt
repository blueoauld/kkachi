package com.blueoauld.server.activity.entity

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

    @Column(name = "sender_id", nullable = false)
    val senderId: Long,

    @Column(name = "receiver_id", nullable = false)
    val receiverId: Long,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
