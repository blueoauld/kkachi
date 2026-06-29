package com.blueoauld.server.member.application

import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.member.entity.type.ImageType
import com.blueoauld.server.member.repository.MemberImageRepository
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.stereotype.Component
import java.time.Year
import java.time.ZoneId

@Component
class MemberCardReader(

    private val memberRepository: MemberRepository,
    private val memberImageRepository: MemberImageRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")
        private const val REPRESENTATIVE_IMAGE_ORDER = 0
    }

    fun readByIds(memberIds: Collection<Long>): Map<Long, MemberCard> {
        if (memberIds.isEmpty()) {
            return emptyMap()
        }

        val representativeImageByMemberId = memberImageRepository
            .findByMemberIdInAndTypeAndDisplayOrder(memberIds, ImageType.PUBLIC, REPRESENTATIVE_IMAGE_ORDER)
            .associateBy { it.memberId }

        val currentYear = Year.now(KST).value
        return memberRepository.findAllById(memberIds).associate { member ->
            member.id to MemberCard(
                memberId = member.id,
                profileImageUrl = representativeImageByMemberId[member.id]?.let {
                    imageStorage.generatePresignedDownloadUrl(it.objectKey)
                },
                nickname = member.nickname,
                gender = member.gender,
                age = currentYear - member.birthYear,
                comment = member.comment,
            )
        }
    }
}
