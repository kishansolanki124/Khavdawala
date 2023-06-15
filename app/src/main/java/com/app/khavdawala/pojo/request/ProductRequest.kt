package com.app.khavdawala.pojo.request

data class ProductRequest(
    val cid: Int = 0,
    val start: Int = 0,
    val end: Int = 0,
    val user_mobile: String = "",
    val pid: String = "",
    val search: String = ""
)