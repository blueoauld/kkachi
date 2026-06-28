package com.blueoauld.server.member.application.request

import com.blueoauld.server.member.entity.type.ImageType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UpdateImagesRequest(

    @field:NotNull(message = "이미지 종류는 필수입니다.")
    var type: ImageType,

    @field:Size(max = 5, message = "이미지는 최대 5장까지 등록할 수 있습니다.")
    val images: List<ImageItem> = emptyList(),
)

data class ImageItem(

    val imageId: Long? = null,
    val objectKey: String? = null,
)
