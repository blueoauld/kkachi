package com.blueoauld.server.report.entity

import com.blueoauld.server.global.security.PhoneAttributeConverter
import com.blueoauld.server.report.entity.type.ReportType
import jakarta.persistence.*
import java.time.Instant

@Entity
class Report(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "reporter_id", nullable = false)
    val reporterId: Long,

    @Convert(converter = PhoneAttributeConverter::class)
    @Column(name = "reporter_phone", length = 100, nullable = false)
    val reporterPhone: String,

    @Column(name = "reported_id", nullable = false)
    val reportedId: Long,

    @Convert(converter = PhoneAttributeConverter::class)
    @Column(name = "reported_phone", length = 100, nullable = false)
    val reportedPhone: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: ReportType,

    @Column(name = "reason", length = 1000)
    val reason: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
