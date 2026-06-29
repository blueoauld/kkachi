package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.entity.Block
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BlockRepository : JpaRepository<Block, Long> {

    fun existsByBlockerIdAndBlockedId(blockerId: Long, blockedId: Long): Boolean

    fun deleteByBlockerIdAndBlockedId(blockerId: Long, blockedId: Long): Long

    @Query(
        """
        SELECT b
        FROM Block b
        WHERE b.blockerId = :blockerId
          AND (:cursor IS NULL OR b.id < :cursor)
        ORDER BY b.id DESC
        """,
    )
    fun findBlocks(
        @Param("blockerId") blockerId: Long,
        @Param("cursor") cursor: Long?,
        pageable: Pageable,
    ): List<Block>
}
