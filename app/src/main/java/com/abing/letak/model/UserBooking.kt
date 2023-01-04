package com.abing.letak.model

import java.util.Date

data class UserBooking(
    var bookingId: String? = null,
    var lotId: String? = null,
    var spaceId: String? = null,
    var spaceType: String? = null,
    var parkingPeriodMinute: Int? = null,
    var parkingStart: Date? = null,
    var parkingEnd: Date? = null,
    var eWalletType: String? = null,
    var vecPlate: String? = null,
    var parkingStartMilis: Long? = null
)
