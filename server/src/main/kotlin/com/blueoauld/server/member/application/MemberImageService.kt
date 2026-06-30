package com.blueoauld.server.member.application

import com.blueoauld.server.activity.repository.SecretImageAccessRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.member.application.request.CreateImageUploadUrlRequest
import com.blueoauld.server.member.application.request.UpdateImagesRequest
import com.blueoauld.server.member.application.response.ImageUploadUrlResponse
import com.blueoauld.server.member.application.response.SecretImageResponse
import com.blueoauld.server.member.entity.MemberImage
import com.blueoauld.server.member.entity.type.ImageType
import com.blueoauld.server.member.repository.MemberImageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberImageService(

    private val memberImageRepository: MemberImageRepository,
    private val secretImageAccessRepository: SecretImageAccessRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private const val MAX_IMAGE_SIZE = 20L * 1024 * 1024 // 20MB
        private const val TMP_KEY_PREFIX = "tmp/members/"

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

        val objectKey = "$TMP_KEY_PREFIX$memberId/${UUID.randomUUID()}.$extension"
        val uploadUrl = imageStorage.generatePresignedUploadUrl(objectKey, request.contentType, request.contentLength)

        return ImageUploadUrlResponse(objectKey, uploadUrl)
    }

    @Transactional
    fun updateImages(memberId: Long, request: UpdateImagesRequest) {
        val currentImages = memberImageRepository.findByMemberIdAndType(memberId, request.type)
        val currentImageById = currentImages.associateBy { it.id }
        val keptImageIds = HashSet<Long>()

        request.images.forEachIndexed { order, item ->
            if ((item.imageId == null) == (item.objectKey == null)) {
                throw BusinessException(ErrorCode.INVALID_INPUT)
            }

            if (item.imageId != null) {
                val image = currentImageById[item.imageId] ?: throw BusinessException(ErrorCode.IMAGE_NOT_FOUND)
                if (!keptImageIds.add(item.imageId)) {
                    throw BusinessException(ErrorCode.INVALID_INPUT)
                }

                image.displayOrder = order
            } else {
                registerNewImage(memberId, request.type, item.objectKey!!, order)
            }
        }

        currentImages
            .filter { it.id !in keptImageIds }
            .forEach { image ->
                memberImageRepository.delete(image)
                imageStorage.delete(image.objectKey)
            }
    }

    @Transactional
    fun removeImages(memberId: Long, type: ImageType) {
        memberImageRepository.findByMemberIdAndType(memberId, type).forEach { image ->
            memberImageRepository.delete(image)
            imageStorage.delete(image.objectKey)
        }
    }

    @Transactional(readOnly = true)
    fun getSecretImages(viewerId: Long, targetId: Long): SecretImageResponse {
        if (viewerId != targetId && !secretImageAccessRepository.existsByOwnerIdAndViewerId(targetId, viewerId)) {
            throw BusinessException(ErrorCode.SECRET_IMAGE_ACCESS_DENIED)
        }

        val imageUrls = memberImageRepository.findByMemberIdAndType(targetId, ImageType.SECRET)
            .sortedBy { it.displayOrder }
            .map { imageStorage.generatePresignedDownloadUrl(it.objectKey) }

        return SecretImageResponse(imageUrls)
    }

    private fun registerNewImage(memberId: Long, type: ImageType, tmpObjectKey: String, order: Int) {
        if (!tmpObjectKey.startsWith("$TMP_KEY_PREFIX$memberId/")) {
            throw BusinessException(ErrorCode.INVALID_IMAGE_OBJECT_KEY)
        }

        val objectKey = permanentKey(memberId, type, tmpObjectKey)
        if (memberImageRepository.existsByObjectKey(objectKey)) {
            throw BusinessException(ErrorCode.IMAGE_ALREADY_REGISTERED)
        }

        imageStorage.copy(tmpObjectKey, objectKey)

        memberImageRepository.save(
            MemberImage(
                memberId = memberId,
                type = type,
                objectKey = objectKey,
                displayOrder = order,
            ),
        )
    }

    private fun permanentKey(memberId: Long, type: ImageType, tmpObjectKey: String) =
        "members/$memberId/${type.name.lowercase()}/${tmpObjectKey.substringAfterLast('/')}"
}
