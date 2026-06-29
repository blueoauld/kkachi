package com.blueoauld.server.report.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "report_image")
class ReportImage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "report_id", nullable = false)
    val reportId: Long,

    @Column(name = "object_key", nullable = false, unique = true)
    val objectKey: String,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
