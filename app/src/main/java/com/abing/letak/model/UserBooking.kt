package com.abing.letak.model

import java.util.Date

data class UserBooking(
    var bookingId: String? = null,
    var lotId: String? = null,
    val spaceId: String? = null,
    val spaceType: String? = null,
    val parkingPeriodMinute: Int? = null,
    val parkingStart: Date? = null,
    val parkingEnd: Date? = null
)
