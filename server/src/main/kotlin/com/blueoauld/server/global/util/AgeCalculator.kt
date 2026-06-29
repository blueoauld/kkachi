package com.blueoauld.server.global.util

import java.time.Year
import java.time.ZoneId

object AgeCalculator {

    private val KST = ZoneId.of("Asia/Seoul")

    fun fromBirthYear(birthYear: Int): Int = Year.now(KST).value - birthYear
}
