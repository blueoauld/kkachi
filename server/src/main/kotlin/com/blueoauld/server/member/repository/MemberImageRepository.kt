package com.blueoauld.server.member.repository

import com.blueoauld.server.member.entity.MemberImage
import com.blueoauld.server.member.entity.type.ImageType
import org.springframework.data.jpa.repository.JpaRepository

interface MemberImageRepository : JpaRepository<MemberImage, Long> {

    fun findByMemberIdAndType(memberId: Long, type: ImageType): List<MemberImage>

    fun existsByObjectKey(objectKey: String): Boolean
}
