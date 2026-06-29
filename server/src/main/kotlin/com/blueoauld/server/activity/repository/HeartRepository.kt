package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.entity.Heart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface HeartRepository : JpaRepository<Heart, Long>, HeartCustomRepository {

    fun existsBySenderIdAndReceiverId(senderId: Long, receiverId: Long): Boolean

    fun deleteBySenderIdAndReceiverId(senderId: Long, receiverId: Long): Long

    fun countByReceiverId(receiverId: Long): Long

    @Query(
        """
        SELECT h.receiverId AS memberId, COUNT(h) AS count
        FROM Heart h
        WHERE h.receiverId IN (:memberIds)
        GROUP BY h.receiverId
        """,
    )
    fun countByReceiverIds(@Param("memberIds") memberIds: Collection<Long>): List<HeartCountProjection>
}

interface HeartCountProjection {

    val memberId: Long
    val count: Long
}
