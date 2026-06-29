package com.blueoauld.server.point.application

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.member.repository.MemberRepository
import com.blueoauld.server.point.application.response.PointBalanceResponse
import com.blueoauld.server.point.application.response.PointHistoryResponse
import com.blueoauld.server.point.entity.PointHistory
import com.blueoauld.server.point.entity.PointWallet
import com.blueoauld.server.point.entity.type.PointTransactionType
import com.blueoauld.server.point.repository.PointHistoryRepository
import com.blueoauld.server.point.repository.PointWalletRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(

    private val pointWalletRepository: PointWalletRepository,
    private val pointHistoryRepository: PointHistoryRepository,
    private val memberRepository: MemberRepository,
) {

    @Transactional(readOnly = true)
    fun getBalance(memberId: Long): PointBalanceResponse {
        val balance = pointWalletRepository.findByMemberId(memberId)?.balance ?: 0
        return PointBalanceResponse(balance)
    }

    @Transactional
    fun earn(memberId: Long, amount: Long, description: String): PointBalanceResponse {
        if (amount <= 0) {
            throw BusinessException(ErrorCode.INVALID_POINT_AMOUNT)
        }

        val wallet = pointWalletRepository.findByMemberIdForUpdate(memberId) ?: createWallet(memberId)
        val balance = wallet.earn(amount)

        pointHistoryRepository.save(
            PointHistory(
                memberId = memberId,
                type = PointTransactionType.EARN,
                amount = amount,
                balanceAfter = balance,
                description = description,
            ),
        )

        return PointBalanceResponse(balance)
    }

    @Transactional
    fun spend(memberId: Long, amount: Long, description: String): PointBalanceResponse {
        if (amount <= 0) {
            throw BusinessException(ErrorCode.INVALID_POINT_AMOUNT)
        }

        val wallet = pointWalletRepository.findByMemberIdForUpdate(memberId)
            ?: throw BusinessException(ErrorCode.INSUFFICIENT_POINT)
        val balance = wallet.spend(amount)

        pointHistoryRepository.save(
            PointHistory(
                memberId = memberId,
                type = PointTransactionType.SPEND,
                amount = -amount,
                balanceAfter = balance,
                description = description,
            ),
        )

        return PointBalanceResponse(balance)
    }

    @Transactional(readOnly = true)
    fun getHistories(memberId: Long, cursor: Long?, size: Int): CursorResponse<PointHistoryResponse> {
        val results = pointHistoryRepository.findHistories(memberId, cursor, size + 1)
        return CursorResponse.of(results, size, { it.id }) { history ->
            PointHistoryResponse(
                historyId = history.id,
                type = history.type,
                amount = history.amount,
                balanceAfter = history.balanceAfter,
                description = history.description,
                createdAt = history.createdAt,
            )
        }
    }

    private fun createWallet(memberId: Long): PointWallet {
        if (!memberRepository.existsById(memberId)) {
            throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        }

        return pointWalletRepository.save(PointWallet(memberId = memberId))
    }
}
