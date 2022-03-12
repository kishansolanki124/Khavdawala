package com.app.khavdawala.pojo.response

data class AddOrderResponse(
    var message: String = "",
    var order_id: Int = 0,
    var order_no: String = "",
    var razorpay_orderid: String = "",
    var status: String = ""
)