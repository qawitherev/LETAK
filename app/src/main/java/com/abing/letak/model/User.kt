package com.abing.letak.model

data class User(
    val userId: String? = null,
    val userFirstName: String? = null,
    val userLastName: String? = null,
    val userEmail: String? = null,
    val userPhoneNum: String? = null
)

// TODO: use viewmodel to store the user id so that can access from anywhere