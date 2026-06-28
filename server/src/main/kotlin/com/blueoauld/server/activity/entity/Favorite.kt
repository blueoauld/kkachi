package com.blueoauld.server.activity.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "favorite",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["owner_id", "target_id"]),
    ],
)
class Favorite(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "owner_id", nullable = false)
    val ownerId: Long,

    @Column(name = "target_id", nullable = false)
    val targetId: Long,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
