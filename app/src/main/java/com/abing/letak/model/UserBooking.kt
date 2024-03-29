package com.abing.letak.model

import java.util.Date

data class UserBooking(
    var bookingId: String? = null,
    var lotId: String? = null,
    var spaceId: String? = null,
    var spaceType: String? = null,
    var parkingPeriodMinute: Int? = null,
    var parkingStart: String? = null,
    var parkingEnd: String? = null,
    var eWalletType: String? = null,
    var vecPlate: String? = null,
    var parkingStartMilis: Long? = null,
    var feePaid: Double? = null
)
