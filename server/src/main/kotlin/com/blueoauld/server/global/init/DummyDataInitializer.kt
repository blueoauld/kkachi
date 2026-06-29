package com.blueoauld.server.global.init

import com.blueoauld.server.activity.entity.Favorite
import com.blueoauld.server.activity.entity.Heart
import com.blueoauld.server.activity.entity.SecretImageAccess
import com.blueoauld.server.activity.repository.BlockRepository
import com.blueoauld.server.activity.repository.FavoriteRepository
import com.blueoauld.server.activity.repository.HeartRepository
import com.blueoauld.server.activity.repository.SecretImageAccessRepository
import com.blueoauld.server.member.entity.Member
import com.blueoauld.server.member.entity.type.GenderType
import com.blueoauld.server.member.repository.MemberRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Profile("local")
@Component
class DummyDataInitializer(

    private val memberRepository: MemberRepository,
    private val heartRepository: HeartRepository,
    private val favoriteRepository: FavoriteRepository,
    private val blockRepository: BlockRepository,
    private val secretImageAccessRepository: SecretImageAccessRepository,
    private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {

    companion object {
        private const val SRID_WGS84 = 4326
        private const val DUMMY_MEMBER_COUNT = 30
        private const val BASE_LATITUDE = 37.5665
        private const val BASE_LONGITUDE = 126.9780
    }

    private val log = LoggerFactory.getLogger(javaClass)
    private val geometryFactory = GeometryFactory(PrecisionModel(), SRID_WGS84)

    @Transactional
    override fun run(args: ApplicationArguments) {
        if (memberRepository.count() > 0) {
            log.info("이미 데이터가 존재하여 더미 데이터 초기화를 건너뜁니다.")
            return
        }

        val members = (1..DUMMY_MEMBER_COUNT).map { i ->
            Member(
                phone = "010%08d".format(i),
                password = passwordEncoder.encode("1234")!!,
                gender = if (i % 2 == 0) GenderType.MALE else GenderType.FEMALE,
                nickname = "더미$i",
                birthYear = 1980 + (i % 26),
                bio = "더미 회원 $i 의 자기소개",
                comment = "더미 회원 $i 의 코멘트",
                location = point(BASE_LATITUDE + i * 0.001, BASE_LONGITUDE + i * 0.001),
            )
        }
        val savedMembers = memberRepository.saveAll(members)
        val receiver = savedMembers.first()

        val heartsAsReceiver = savedMembers.drop(1).map { Heart(senderId = it.id, receiverId = receiver.id) }
        val heartsAsSender = savedMembers.drop(1).map { Heart(senderId = receiver.id, receiverId = it.id) }
        heartRepository.saveAll(heartsAsReceiver + heartsAsSender)

        val favoritesAsTarget = savedMembers.drop(1).map { Favorite(ownerId = it.id, targetId = receiver.id) }
        val favoritesAsOwner = savedMembers.drop(1).map { Favorite(ownerId = receiver.id, targetId = it.id) }
        favoriteRepository.saveAll(favoritesAsTarget + favoritesAsOwner)

        val secretImageViewers = savedMembers.drop(1).map { SecretImageAccess(ownerId = receiver.id, viewerId = it.id) }
        val secretImageOwners = savedMembers.drop(1).map { SecretImageAccess(ownerId = it.id, viewerId = receiver.id) }
        secretImageAccessRepository.saveAll(secretImageViewers + secretImageOwners)

        log.info("더미 회원 {}명", savedMembers.size)
    }

    private fun point(latitude: Double, longitude: Double): Point =
        geometryFactory.createPoint(Coordinate(longitude, latitude))
}
