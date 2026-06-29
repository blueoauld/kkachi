package com.blueoauld.server.report.application

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.member.repository.MemberRepository
import com.blueoauld.server.report.application.request.CreateReportImageUploadUrlRequest
import com.blueoauld.server.report.application.request.CreateReportRequest
import com.blueoauld.server.report.application.response.ReportImageUploadUrlResponse
import com.blueoauld.server.report.entity.Report
import com.blueoauld.server.report.entity.ReportImage
import com.blueoauld.server.report.repository.ReportImageRepository
import com.blueoauld.server.report.repository.ReportRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ReportService(

    private val reportRepository: ReportRepository,
    private val reportImageRepository: ReportImageRepository,
    private val memberRepository: MemberRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private const val MAX_IMAGE_SIZE = 20L * 1024 * 1024 // 20MB
        private const val TMP_KEY_PREFIX = "tmp/reports/"

        private val ALLOWED_CONTENT_TYPES = mapOf(
            "image/jpeg" to "jpg",
            "image/png" to "png",
            "image/webp" to "webp",
        )
    }

    fun createUploadUrl(memberId: Long, request: CreateReportImageUploadUrlRequest): ReportImageUploadUrlResponse {
        val extension = ALLOWED_CONTENT_TYPES[request.contentType]
            ?: throw BusinessException(ErrorCode.UNSUPPORTED_IMAGE_TYPE)

        if (request.contentLength > MAX_IMAGE_SIZE) {
            throw BusinessException(ErrorCode.IMAGE_SIZE_EXCEEDED)
        }

        val objectKey = "$TMP_KEY_PREFIX$memberId/${UUID.randomUUID()}.$extension"
        val uploadUrl = imageStorage.generatePresignedUploadUrl(objectKey, request.contentType, request.contentLength)

        return ReportImageUploadUrlResponse(objectKey, uploadUrl)
    }

    @Transactional
    fun report(reporterId: Long, request: CreateReportRequest) {
        if (reporterId == request.reportedId) {
            throw BusinessException(ErrorCode.CANNOT_REPORT_SELF)
        }

        val reporter = memberRepository.findByIdOrNull(reporterId)
            ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        val reported = memberRepository.findByIdOrNull(request.reportedId)
            ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        val report = reportRepository.save(
            Report(
                reporterId = reporter.id,
                reporterPhone = reporter.phone,
                reportedId = reported.id,
                reportedPhone = reported.phone,
                type = request.type,
                reason = request.reason,
            ),
        )

        request.objectKeys.forEachIndexed { order, tmpObjectKey ->
            registerImage(reporterId, report.id, tmpObjectKey, order)
        }
    }

    private fun registerImage(reporterId: Long, reportId: Long, tmpObjectKey: String, order: Int) {
        if (!tmpObjectKey.startsWith("$TMP_KEY_PREFIX$reporterId/")) {
            throw BusinessException(ErrorCode.INVALID_IMAGE_OBJECT_KEY)
        }

        val objectKey = "reports/$reportId/${tmpObjectKey.substringAfterLast('/')}"
        if (reportImageRepository.existsByObjectKey(objectKey)) {
            throw BusinessException(ErrorCode.IMAGE_ALREADY_REGISTERED)
        }

        imageStorage.copy(tmpObjectKey, objectKey)

        reportImageRepository.save(
            ReportImage(
                reportId = reportId,
                objectKey = objectKey,
                displayOrder = order,
            ),
        )
    }
}
