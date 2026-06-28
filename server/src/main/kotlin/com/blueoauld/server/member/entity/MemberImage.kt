package com.blueoauld.server.member.entity

import com.blueoauld.server.member.entity.type.ImageType
import jakarta.persistence.*
import java.time.Instant

@Entity
class MemberImage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "member_id", nullable = false)
    val memberId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: ImageType,

    @Column(name = "object_key", nullable = false, unique = true)
    val objectKey: String,

    @Column(name = "display_order", nullable = false)
    var displayOrder: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
) {

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
