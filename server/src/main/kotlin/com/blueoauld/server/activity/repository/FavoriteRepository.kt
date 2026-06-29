package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.entity.Favorite
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FavoriteRepository : JpaRepository<Favorite, Long> {

    fun existsByOwnerIdAndTargetId(ownerId: Long, targetId: Long): Boolean

    fun deleteByOwnerIdAndTargetId(ownerId: Long, targetId: Long): Long

    @Query(
        """
        SELECT f
        FROM Favorite f
        WHERE f.ownerId = :ownerId
          AND (:cursor IS NULL OR f.id < :cursor)
        ORDER BY f.id DESC
        """,
    )
    fun findFavorites(
        @Param("ownerId") ownerId: Long,
        @Param("cursor") cursor: Long?,
        pageable: Pageable,
    ): List<Favorite>

    @Query(
        """
        SELECT f
        FROM Favorite f
        WHERE f.targetId = :targetId
          AND (:cursor IS NULL OR f.id < :cursor)
        ORDER BY f.id DESC
        """,
    )
    fun findReceivedFavorites(
        @Param("targetId") targetId: Long,
        @Param("cursor") cursor: Long?,
        pageable: Pageable,
    ): List<Favorite>
}
