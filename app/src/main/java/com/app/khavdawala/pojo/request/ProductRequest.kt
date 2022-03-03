package com.app.khavdawala.pojo.request

data class ProductRequest(
    val cid: Int,
    val start: Int,
    val end: Int,
    val user_mobile: String
)