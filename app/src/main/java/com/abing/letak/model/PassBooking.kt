package com.abing.letak.model

data class PassBooking(
    var passId: String? = null,
    var lotId: String? = null,
    var lotName: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var eWalletType: String? = null,
    var spaceId: String? = null,
    var isParked: Boolean? = null
)