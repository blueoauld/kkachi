package com.blueoauld.server.suspension.entity

import com.blueoauld.server.global.security.PhoneAttributeConverter
import jakarta.persistence.*
import java.time.Instant

@Entity
class Suspension(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Convert(converter = PhoneAttributeConverter::class)
    @Column(name = "phone", length = 100, nullable = false)
    val phone: String,

    @Column(name = "reason", length = 1000, nullable = false)
    val reason: String,

    @Column(name = "suspended_until", nullable = false)
    val suspendedUntil: Instant,

    @Column(name = "released_at")
    var releasedAt: Instant? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    fun release() {
        this.releasedAt = Instant.now()
    }

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
