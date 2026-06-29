package com.blueoauld.server.report.application.request

import com.blueoauld.server.report.entity.type.ReportType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateReportRequest(

    @field:NotNull(message = "신고 대상은 필수입니다.")
    var reportedId: Long,

    @field:NotNull(message = "신고 유형은 필수입니다.")
    var type: ReportType,

    @field:Size(max = 1000, message = "신고 사유는 최대 1000자까지 입력할 수 있습니다.")
    val reason: String? = null,

    @field:Size(max = 5, message = "증거 이미지는 최대 5장까지 첨부할 수 있습니다.")
    val objectKeys: List<String> = emptyList(),
)
