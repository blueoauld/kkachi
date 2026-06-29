package com.blueoauld.server.point.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.point.application.AttendanceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class AttendanceController(

    private val attendanceService: AttendanceService,
) {

    @PostMapping("/v1/attendances")
    fun checkIn(
        @LoginMember memberId: Long,
    ): ResponseEntity<Unit> {
        attendanceService.checkIn(memberId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
