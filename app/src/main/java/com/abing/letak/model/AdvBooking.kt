package com.abing.letak.model

import java.util.*

data class AdvBooking(
    var advBookingId: String? = null,
    var lotId: String? = null,
    var spaceId: String? = null,
    var spaceType: String? = null,
    var parkingPeriodMinute: String? = null,
    var parkingStart: Date? = null,
    var parkingEnd: Date? = null,
    var vecPlate: String? = null,
    var eWalletType: String? = null,
)
