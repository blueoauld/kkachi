package com.blueoauld.server.global.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CopyObjectRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Component
class R2ImageStorage(

    @Value("\${r2.bucket}") private val bucket: String,
    @Value("\${r2.presigned-url-expiration}") private val presignedUrlExpiration: Long,

    private val s3Presigner: S3Presigner,
    private val s3Client: S3Client,
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

    override fun copy(sourceKey: String, destinationKey: String) {
        s3Client.copyObject(
            CopyObjectRequest.builder()
                .sourceBucket(bucket)
                .sourceKey(sourceKey)
                .destinationBucket(bucket)
                .destinationKey(destinationKey)
                .build(),
        )
    }

    override fun delete(objectKey: String) {
        s3Client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build(),
        )
    }
}
