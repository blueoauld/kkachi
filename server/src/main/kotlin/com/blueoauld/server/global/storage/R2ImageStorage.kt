package com.blueoauld.server.global.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Component
class R2ImageStorage(

    @Value("\${r2.bucket}") private val bucket: String,
    @Value("\${r2.presigned-url-expiration}") private val presignedUrlExpiration: Long,

    private val s3Presigner: S3Presigner,
) : ImageStorage {

    override fun generatePresignedUploadUrl(objectKey: String, contentType: String, contentLength: Long): String {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(objectKey)
            .contentType(contentType)
            .contentLength(contentLength)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMillis(presignedUrlExpiration))
            .putObjectRequest(putObjectRequest)
            .build()

        return s3Presigner.presignPutObject(presignRequest).url().toString()
    }
}
