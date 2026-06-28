package com.blueoauld.server.member.application

import com.blueoauld.server.activity.repository.BlockRepository
import com.blueoauld.server.activity.repository.FavoriteRepository
import com.blueoauld.server.activity.repository.HeartRepository
import com.blueoauld.server.activity.repository.SecretImageAccessRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.member.application.request.UpdateCommentRequest
import com.blueoauld.server.member.application.request.UpdateLocationRequest
import com.blueoauld.server.member.application.request.UpdateProfileRequest
import com.blueoauld.server.member.application.response.MemberProfileResponse
import com.blueoauld.server.member.application.response.MyProfileResponse
import com.blueoauld.server.member.entity.type.ImageType
import com.blueoauld.server.member.repository.MemberImageRepository
import com.blueoauld.server.member.repository.MemberRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import java.time.ZoneId

@Service
class MemberService(

    private val memberRepository: MemberRepository,
    private val memberImageRepository: MemberImageRepository,
    private val heartRepository: HeartRepository,
    private val favoriteRepository: FavoriteRepository,
    private val blockRepository: BlockRepository,
    private val secretImageAccessRepository: SecretImageAccessRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")
        private const val MIN_AGE = 19
        private const val MAX_AGE = 50

        private const val SRID_WGS84 = 4326
        private val GEOMETRY_FACTORY = GeometryFactory(PrecisionModel(), SRID_WGS84)
    }

    @Transactional
    fun updateProfile(memberId: Long, request: UpdateProfileRequest) {
        validateBirthYear(request.birthYear)

        val member = memberRepository.findByIdOrNull(memberId) ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        if (memberRepository.existsByNicknameAndIdNot(request.nickname, memberId)) {
            throw BusinessException(ErrorCode.DUPLICATE_NICKNAME)
        }

        member.updateProfile(request.nickname, request.birthYear, request.bio)
    }

    @Transactional
    fun updateLocation(memberId: Long, request: UpdateLocationRequest) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        member.updateLocation(toLocation(request.latitude, request.longitude))
    }

    @Transactional
    fun updateComment(memberId: Long, request: UpdateCommentRequest) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        member.updateComment(request.comment)
    }

    @Transactional
    fun bump(memberId: Long) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        member.bump()
    }

    @Transactional(readOnly = true)
    fun getMyProfile(memberId: Long): MyProfileResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        val publicImageUrls = memberImageRepository.findByMemberIdAndType(memberId, ImageType.PUBLIC)
            .sortedBy { it.displayOrder }
            .map { imageStorage.generatePresignedDownloadUrl(it.objectKey) }

        return MyProfileResponse(
            memberId = member.id,
            nickname = member.nickname,
            gender = member.gender,
            age = Year.now(KST).value - member.birthYear,
            bio = member.bio,
            heartCount = heartRepository.countByReceiverId(memberId),
            publicImageUrls = publicImageUrls,
        )
    }

    @Transactional(readOnly = true)
    fun getMemberProfile(viewerId: Long, targetId: Long): MemberProfileResponse {
        val target = memberRepository.findByIdOrNull(targetId) ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        val publicImageUrls = memberImageRepository.findByMemberIdAndType(targetId, ImageType.PUBLIC)
            .sortedBy { it.displayOrder }
            .map { imageStorage.generatePresignedDownloadUrl(it.objectKey) }

        return MemberProfileResponse(
            memberId = target.id,
            nickname = target.nickname,
            gender = target.gender,
            age = Year.now(KST).value - target.birthYear,
            heartCount = heartRepository.countByReceiverId(targetId),
            updatedAt = target.updatedAt,
            distance = memberRepository.calculateDistanceMeters(viewerId, targetId),
            favorited = favoriteRepository.existsByOwnerIdAndTargetId(viewerId, targetId),
            hearted = heartRepository.existsBySenderIdAndReceiverId(viewerId, targetId),
            secretImageOpenedByMe = secretImageAccessRepository.existsByOwnerIdAndViewerId(viewerId, targetId),
            secretImageOpenedToMe = secretImageAccessRepository.existsByOwnerIdAndViewerId(targetId, viewerId),
            secretImageCount = memberImageRepository.countByMemberIdAndType(targetId, ImageType.SECRET),
            blocked = blockRepository.existsByBlockerIdAndBlockedId(viewerId, targetId),
            publicImageUrls = publicImageUrls,
        )
    }

    private fun validateBirthYear(birthYear: Int) {
        val age = Year.now(KST).value - birthYear
        if (age !in MIN_AGE..MAX_AGE) {
            throw BusinessException(ErrorCode.INVALID_BIRTH_YEAR)
        }
    }

    private fun toLocation(latitude: Double?, longitude: Double?): Point? =
        if (latitude != null && longitude != null) {
            GEOMETRY_FACTORY.createPoint(Coordinate(longitude, latitude))
        } else {
            null
        }
}
