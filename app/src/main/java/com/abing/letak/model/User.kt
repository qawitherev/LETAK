package com.abing.letak.model

data class User(
    val userId: String,
    val userFirstName: String,
    val userLastName: String,
    val userEmail: String? = null,
    val userPhoneNum: String? = null
)

// TODO: use viewmodel to store the user id so that can access from anywhere