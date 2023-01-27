package com.abing.letak.model

data class User(
    val userId: String? = null,
    val userFirstName: String? = null,
    val userLastName: String? = null,
    val userEmail: String? = null,
    val userPhoneNum: String? = null,
    val orderNowStatus: Boolean? = null,
    val advanceBookingStatus: Boolean? = null,
    val monthlyPassStatus: Boolean? = null,
    val activeBookingId: String? = null,
    val activeMonthlyId: String? = null,
    val activePassId: String? = null
)