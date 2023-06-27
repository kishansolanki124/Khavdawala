package com.app.khavdawala.pojo.request

data class RegisterRequest(
    val name: String,
    val mobile: String,
    val birth_date: String,
    val deviceId: String,
    val platform: String = "android"
)