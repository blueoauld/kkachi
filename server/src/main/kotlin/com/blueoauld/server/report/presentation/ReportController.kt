package com.blueoauld.server.report.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.report.application.ReportService
import com.blueoauld.server.report.application.request.CreateReportImageUploadUrlRequest
import com.blueoauld.server.report.application.request.CreateReportRequest
import com.blueoauld.server.report.application.response.ReportImageUploadUrlResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class ReportController(

    private val reportService: ReportService,
) {

    @PostMapping("/v1/reports/images/presigned-url")
    fun createUploadUrl(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: CreateReportImageUploadUrlRequest,
    ): ResponseEntity<ReportImageUploadUrlResponse> {
        val response = reportService.createUploadUrl(memberId, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/v1/reports")
    fun report(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: CreateReportRequest,
    ): ResponseEntity<Unit> {
        reportService.report(memberId, request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
