package com.blueoauld.server.point.repository

import com.blueoauld.server.point.entity.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AttendanceRepository : JpaRepository<Attendance, Long> {

    fun existsByMemberIdAndCheckDate(memberId: Long, checkDate: LocalDate): Boolean
}
