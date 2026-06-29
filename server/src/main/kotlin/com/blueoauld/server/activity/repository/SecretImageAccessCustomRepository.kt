package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.repository.result.ActivityResult

interface SecretImageAccessCustomRepository {

    fun findSecretImageViewers(
        ownerId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult>

    fun findSecretImageOwners(
        viewerId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult>
}
