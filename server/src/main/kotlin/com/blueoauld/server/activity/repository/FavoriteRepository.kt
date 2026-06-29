package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.entity.Favorite
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteRepository : JpaRepository<Favorite, Long>, FavoriteCustomRepository {

    fun existsByOwnerIdAndTargetId(ownerId: Long, targetId: Long): Boolean

    fun deleteByOwnerIdAndTargetId(ownerId: Long, targetId: Long): Long
}
