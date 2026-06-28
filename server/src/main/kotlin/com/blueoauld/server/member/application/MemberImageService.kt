package com.blueoauld.server.member.application

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.member.application.request.CreateImageUploadUrlRequest
import com.blueoauld.server.member.application.response.ImageUploadUrlResponse
import com.blueoauld.server.member.repository.MemberImageRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberImageService(

    private val memberImageRepository: MemberImageRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private const val MAX_IMAGES_PER_TYPE = 5L
        private const val MAX_IMAGE_SIZE = 20L * 1024 * 1024 // 20MB

        private val ALLOWED_CONTENT_TYPES = mapOf(
            "image/jpeg" to "jpg",
            "image/png" to "png",
            "image/webp" to "webp",
        )
    }

    fun createUploadUrl(memberId: Long, request: CreateImageUploadUrlRequest): ImageUploadUrlResponse {
        val extension = ALLOWED_CONTENT_TYPES[request.contentType]
            ?: throw BusinessException(ErrorCode.UNSUPPORTED_IMAGE_TYPE)

        if (request.contentLength > MAX_IMAGE_SIZE) {
            throw BusinessException(ErrorCode.IMAGE_SIZE_EXCEEDED)
        }

        val currentCount = memberImageRepository.countByMemberIdAndType(memberId, request.type)
        if (currentCount >= MAX_IMAGES_PER_TYPE) {
            throw BusinessException(ErrorCode.IMAGE_LIMIT_EXCEEDED)
        }

        val objectKey = "members/$memberId/${request.type.name.lowercase()}/${UUID.randomUUID()}.$extension"
        val uploadUrl = imageStorage.generatePresignedUploadUrl(objectKey, request.contentType, request.contentLength)

        return ImageUploadUrlResponse(objectKey, uploadUrl)
    }
}
