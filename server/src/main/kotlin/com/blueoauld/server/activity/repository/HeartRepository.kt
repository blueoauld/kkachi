package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.entity.Heart
import org.springframework.data.jpa.repository.JpaRepository

interface HeartRepository : JpaRepository<Heart, Long> {

    fun existsBySenderIdAndReceiverId(senderId: Long, receiverId: Long): Boolean

    fun deleteBySenderIdAndReceiverId(senderId: Long, receiverId: Long): Long

    fun countByReceiverId(receiverId: Long): Long
}
