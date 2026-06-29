package com.blueoauld.server.point.application

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.point.entity.Attendance
import com.blueoauld.server.point.repository.AttendanceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneId

@Service
class AttendanceService(

    private val attendanceRepository: AttendanceRepository,
    private val pointService: PointService,
) {

    companion object {
        private const val ATTENDANCE_POINT = 30L
        private val KST = ZoneId.of("Asia/Seoul")
    }

    @Transactional
    fun checkIn(memberId: Long) {
        val today = LocalDate.now(KST)
        if (attendanceRepository.existsByMemberIdAndCheckDate(memberId, today)) {
            throw BusinessException(ErrorCode.ALREADY_CHECKED_IN)
        }

        attendanceRepository.save(
            Attendance(
                memberId = memberId,
                checkDate = today,
            ),
        )

        pointService.earn(memberId, ATTENDANCE_POINT, "출석 체크")
    }
}
