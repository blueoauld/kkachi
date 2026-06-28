package com.blueoauld.server.global.storage

interface ImageStorage {

    fun generatePresignedUploadUrl(objectKey: String, contentType: String, contentLength: Long): String

    fun copy(sourceKey: String, destinationKey: String)

    fun delete(objectKey: String)
}
