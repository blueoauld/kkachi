package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.entity.Heart
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface HeartRepository : JpaRepository<Heart, Long> {

    fun existsBySenderIdAndReceiverId(senderId: Long, receiverId: Long): Boolean

    fun deleteBySenderIdAndReceiverId(senderId: Long, receiverId: Long): Long

    fun countByReceiverId(receiverId: Long): Long

    @Query(
        """
        SELECT h
        FROM Heart h
        WHERE h.senderId = :senderId
          AND (:cursor IS NULL OR h.id < :cursor)
        ORDER BY h.id DESC
        """,
    )
    fun findSentHearts(
        @Param("senderId") senderId: Long,
        @Param("cursor") cursor: Long?,
        pageable: Pageable,
    ): List<Heart>
}
