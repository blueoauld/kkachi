package com.blueoauld.server.member.application

import com.blueoauld.server.activity.repository.BlockRepository
import com.blueoauld.server.activity.repository.FavoriteRepository
import com.blueoauld.server.activity.repository.HeartRepository
import com.blueoauld.server.activity.repository.SecretImageAccessRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.global.util.AgeCalculator
import com.blueoauld.server.member.application.request.UpdateCommentRequest
import com.blueoauld.server.member.application.request.UpdateLocationRequest
import com.blueoauld.server.member.application.request.UpdateProfileRequest
import com.blueoauld.server.member.application.response.*
import com.blueoauld.server.member.entity.type.GenderType
import com.blueoauld.server.member.entity.type.ImageType
import com.blueoauld.server.member.repository.MemberImageRepository
import com.blueoauld.server.member.repository.MemberRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

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
            age = AgeCalculator.fromBirthYear(member.birthYear),
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
            age = AgeCalculator.fromBirthYear(target.birthYear),
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

    @Transactional(readOnly = true)
    fun getMembers(
        viewerId: Long,
        gender: GenderType?,
        sort: MemberSortType,
        cursor: String?,
        size: Int,
    ): MemberCursorResponse {
        val viewer = memberRepository.findByIdOrNull(viewerId) ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)

        val effectiveSort = if (viewer.hasLocation()) sort else MemberSortType.RECENT
        val members = when (effectiveSort) {
            MemberSortType.RECENT -> {
                val decoded = cursor?.let(::decodeRecentCursor)
                memberRepository.findRecentMembers(viewerId, gender, decoded?.first, decoded?.second, size + 1)
            }

            MemberSortType.DISTANCE -> {
                val decoded = cursor?.let(::decodeDistanceCursor)
                memberRepository.findMembersByDistance(viewerId, gender, decoded?.first, decoded?.second, size + 1)
            }
        }

        val hasNext = members.size > size
        val pageMembers = if (hasNext) members.take(size) else members
        val memberIds = pageMembers.map { it.id }

        if (memberIds.isEmpty()) {
            return MemberCursorResponse(emptyList(), null, false)
        }

        val heartCountByMemberId = heartRepository.countByReceiverIds(memberIds)
            .associate { it.memberId to it.count }
        val distanceByMemberId = if (viewer.hasLocation()) {
            memberRepository.findDistances(viewerId, memberIds).associate { it.id to it.distance }
        } else {
            emptyMap()
        }
        val representativeImageByMemberId = memberImageRepository
            .findByMemberIdInAndTypeAndDisplayOrder(memberIds, ImageType.PUBLIC, 0)
            .associateBy { it.memberId }

        val items = pageMembers.map { member ->
            MemberListResponse(
                memberId = member.id,
                profileImageUrl = representativeImageByMemberId[member.id]?.let {
                    imageStorage.generatePresignedDownloadUrl(it.objectKey)
                },
                nickname = member.nickname,
                age = AgeCalculator.fromBirthYear(member.birthYear),
                gender = member.gender,
                heartCount = heartCountByMemberId[member.id] ?: 0,
                comment = member.comment,
                updatedAt = member.updatedAt,
                distance = distanceByMemberId[member.id],
            )
        }

        val nextCursor = if (!hasNext) {
            null
        } else {
            val last = pageMembers.last()
            when (effectiveSort) {
                MemberSortType.RECENT -> encodeRecentCursor(last.updatedAt, last.id)
                MemberSortType.DISTANCE -> encodeDistanceCursor(distanceByMemberId[last.id], last.id)
            }
        }

        return MemberCursorResponse(items, nextCursor, hasNext)
    }

    @Transactional(readOnly = true)
    fun searchMembers(viewerId: Long, keyword: String, cursor: String?, size: Int): MemberSearchCursorResponse {
        val decoded = cursor?.let(::decodeSearchCursor)
        val results = memberRepository.searchByNickname(
            viewerId,
            keyword,
            decoded?.first,
            decoded?.second,
            PageRequest.of(0, size + 1),
        )

        val hasNext = results.size > size
        val page = if (hasNext) results.take(size) else results
        val memberIds = page.map { it.id }

        if (memberIds.isEmpty()) {
            return MemberSearchCursorResponse(emptyList(), null, false)
        }

        val heartCountByMemberId = heartRepository.countByReceiverIds(memberIds)
            .associate { it.memberId to it.count }
        val representativeImageByMemberId = memberImageRepository
            .findByMemberIdInAndTypeAndDisplayOrder(memberIds, ImageType.PUBLIC, 0)
            .associateBy { it.memberId }

        val items = page.map { member ->
            MemberSearchResponse(
                memberId = member.id,
                nickname = member.nickname,
                profileImageUrl = representativeImageByMemberId[member.id]?.let {
                    imageStorage.generatePresignedDownloadUrl(it.objectKey)
                },
                gender = GenderType.valueOf(member.gender),
                age = AgeCalculator.fromBirthYear(member.birthYear),
                heartCount = heartCountByMemberId[member.id] ?: 0,
                comment = member.comment,
            )
        }

        val nextCursor = if (!hasNext) {
            null
        } else {
            val last = page.last()
            encodeSearchCursor(last.similarity, last.id)
        }

        return MemberSearchCursorResponse(items, nextCursor, hasNext)
    }

    private fun validateBirthYear(birthYear: Int) {
        val age = AgeCalculator.fromBirthYear(birthYear)
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

    private fun encodeRecentCursor(updatedAt: Instant, id: Long): String =
        Base64.getUrlEncoder().encodeToString("$updatedAt|$id".toByteArray())

    private fun decodeRecentCursor(cursor: String): Pair<Instant, Long> {
        val decoded = String(Base64.getUrlDecoder().decode(cursor))
        val (updatedAt, id) = decoded.split("|", limit = 2)
        return Instant.parse(updatedAt) to id.toLong()
    }

    private fun encodeDistanceCursor(distance: Double?, id: Long): String =
        Base64.getUrlEncoder().encodeToString("${distance ?: ""}|$id".toByteArray())

    private fun decodeDistanceCursor(cursor: String): Pair<Double?, Long> {
        val decoded = String(Base64.getUrlDecoder().decode(cursor))
        val (distance, id) = decoded.split("|", limit = 2)
        return distance.ifEmpty { null }?.toDouble() to id.toLong()
    }

    private fun encodeSearchCursor(similarity: Double, id: Long): String =
        Base64.getUrlEncoder().encodeToString("$similarity|$id".toByteArray())

    private fun decodeSearchCursor(cursor: String): Pair<Double, Long> {
        val decoded = String(Base64.getUrlDecoder().decode(cursor))
        val (similarity, id) = decoded.split("|", limit = 2)
        return similarity.toDouble() to id.toLong()
    }
}
