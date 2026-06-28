package com.blueoauld.server.member.entity

import com.blueoauld.server.member.entity.type.Gender
import jakarta.persistence.*
import org.hibernate.annotations.SoftDelete
import org.hibernate.annotations.SoftDeleteType
import org.locationtech.jts.geom.Point
import java.time.Instant

@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.TIMESTAMP)
@Entity
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "profile_url")
    private val profileUrl: String? = null,

    @Column(name = "phone", length = 11, unique = true, nullable = false)
    private val phone: String,

    @Column(name = "nickname", length = 10, unique = true, nullable = false)
    private val nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private val gender: Gender,

    @Column(name = "birth_year", nullable = false)
    private val birthYear: Int,

    @Column(name = "bio", length = 1000, nullable = false)
    private val bio: String = "안녕하세요.",

    @Column(name = "comment", length = 100, nullable = false)
    private val comment: String = "반갑습니다.",

    @Column(name = "location", columnDefinition = "geography(Point, 4326)")
    private val location: Point? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    private var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    private var updatedAt: Instant = Instant.now(),
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