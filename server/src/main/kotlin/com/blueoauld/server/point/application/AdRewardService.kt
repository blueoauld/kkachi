package com.blueoauld.server.point.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdRewardService(

    private val pointService: PointService,
) {

    companion object {
        private const val AD_REWARD_POINT = 15L
    }

    @Transactional
    fun reward(memberId: Long) {
        pointService.earn(memberId, AD_REWARD_POINT, "광고 보상")
    }
}
