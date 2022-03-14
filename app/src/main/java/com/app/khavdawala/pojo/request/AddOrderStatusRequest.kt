package com.app.khavdawala.pojo.request

data class AddOrderStatusRequest(
    val order_no: String,
    val razorpay_orderid: String,
    val razorpay_paymentid: String,
)