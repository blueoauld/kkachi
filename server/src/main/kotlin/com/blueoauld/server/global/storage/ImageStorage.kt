package com.blueoauld.server.global.storage

fun interface ImageStorage {

    fun generatePresignedUploadUrl(objectKey: String, contentType: String, contentLength: Long): String
}
