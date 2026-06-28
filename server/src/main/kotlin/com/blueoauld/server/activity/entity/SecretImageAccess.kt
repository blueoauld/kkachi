package com.blueoauld.server.activity.entity

import com.blueoauld.server.member.entity.Member
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id", nullable = false)
    val viewer: Member,

    @Column(name = "granted_at", nullable = false, updatable = false)
    var grantedAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.grantedAt = Instant.now()
    }
}
