package com.blueoauld.server.member.entity

import com.blueoauld.server.global.security.PhoneAttributeConverter
import com.blueoauld.server.member.entity.type.GenderType
import jakarta.persistence.*
import org.hibernate.annotations.SoftDelete
import org.hibernate.annotations.SoftDeleteType
import org.locationtech.jts.geom.Point
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.util.*

@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.TIMESTAMP)
@Entity
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Convert(converter = PhoneAttributeConverter::class)
    @Column(name = "phone", length = 100, unique = true, nullable = false)
    private val phone: String,

    @Column(name = "nickname", length = 10, unique = true, nullable = false)
    var nickname: String = "닉네임_" + UUID.randomUUID().toString().take(6),

    @Column(name = "password", length = 100, nullable = false)
    private val password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    val gender: GenderType,

    @Column(name = "birth_year", nullable = false)
    var birthYear: Int = 2000,

    @Column(name = "bio", length = 1000, nullable = false)
    var bio: String = "안녕하세요.",

    @Column(name = "comment", length = 100, nullable = false)
    private var comment: String = "반갑습니다.",

    @Column(name = "location", columnDefinition = "geography(Point, 4326)")
    private var location: Point? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    private var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
) {

    fun matchesPassword(rawPassword: String, passwordEncoder: PasswordEncoder): Boolean =
        passwordEncoder.matches(rawPassword, this.password)

    fun updateProfile(nickname: String, birthYear: Int, bio: String) {
        this.nickname = nickname
        this.birthYear = birthYear
        this.bio = bio
    }

    fun updateLocation(location: Point?) {
        this.location = location
    }

    fun updateComment(comment: String) {
        this.comment = comment
    }

    fun bump() {
        this.updatedAt = Instant.now()
    }

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