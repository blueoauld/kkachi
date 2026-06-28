package com.blueoauld.server.member.application.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin

data class UpdateLocationRequest(

    @field:DecimalMin(value = "-90.0", message = "위도는 -90 ~ 90 사이여야 합니다.")
    @field:DecimalMax(value = "90.0", message = "위도는 -90 ~ 90 사이여야 합니다.")
    val latitude: Double?,

    @field:DecimalMin(value = "-180.0", message = "경도는 -180 ~ 180 사이여야 합니다.")
    @field:DecimalMax(value = "180.0", message = "경도는 -180 ~ 180 사이여야 합니다.")
    val longitude: Double?,
)
