package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.entity.SecretImageAccess
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SecretImageAccessRepository : JpaRepository<SecretImageAccess, Long> {

    fun existsByOwnerIdAndViewerId(ownerId: Long, viewerId: Long): Boolean

    fun deleteByOwnerIdAndViewerId(ownerId: Long, viewerId: Long): Long

    @Query(
        """
        SELECT s
        FROM SecretImageAccess s
        WHERE s.ownerId = :ownerId
          AND (:cursor IS NULL OR s.id < :cursor)
        ORDER BY s.id DESC
        """,
    )
    fun findByOwnerId(
        @Param("ownerId") ownerId: Long,
        @Param("cursor") cursor: Long?,
        pageable: Pageable,
    ): List<SecretImageAccess>

    @Query(
        """
        SELECT s
        FROM SecretImageAccess s
        WHERE s.viewerId = :viewerId
          AND (:cursor IS NULL OR s.id < :cursor)
        ORDER BY s.id DESC
        """,
    )
    fun findByViewerId(
        @Param("viewerId") viewerId: Long,
        @Param("cursor") cursor: Long?,
        pageable: Pageable,
    ): List<SecretImageAccess>
}
