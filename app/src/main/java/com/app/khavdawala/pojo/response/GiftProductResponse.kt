package com.app.khavdawala.pojo.response

data class GiftProductResponse(
    var message: String = "",
    var products_list: ArrayList<ProductListResponse.Products> = ArrayList(),
    var status: String = "",
    var total_records: Int = 0
)