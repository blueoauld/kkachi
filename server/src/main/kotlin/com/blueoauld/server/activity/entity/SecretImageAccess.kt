package com.blueoauld.server.activity.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "secret_image_access",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["owner_id", "viewer_id"]),
    ],
)
class SecretImageAccess(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "owner_id", nullable = false)
    val ownerId: Long,

    @Column(name = "viewer_id", nullable = false)
    val viewerId: Long,

    @Column(name = "granted_at", nullable = false, updatable = false)
    var grantedAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.grantedAt = Instant.now()
    }
}
