package com.blueoauld.server.activity.entity

import com.blueoauld.server.member.entity.Member
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    val target: Member,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
