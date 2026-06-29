package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.entity.SecretImageAccess
import org.springframework.data.jpa.repository.JpaRepository

interface SecretImageAccessRepository : JpaRepository<SecretImageAccess, Long>, SecretImageAccessCustomRepository {

    fun existsByOwnerIdAndViewerId(ownerId: Long, viewerId: Long): Boolean

    fun deleteByOwnerIdAndViewerId(ownerId: Long, viewerId: Long): Long
}
