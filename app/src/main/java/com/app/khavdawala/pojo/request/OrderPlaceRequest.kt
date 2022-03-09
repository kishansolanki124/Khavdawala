package com.app.khavdawala.pojo.request

data class OrderPlaceRequest(
    val customer_name: String,
    val customer_email: String,
    val area: String,
    val sub_area: String,
    val address: String,
    val zipcode: String,
    val city: String,
    val state: String,
    val alternate_contact_no: String,
    val mobile_no: String,
    val product_id: String,
    val product_name: String,
    val packing_id: String,
    val packing_weight: String,
    val packing_weight_type: String,
    val packing_quantity: String,
    val packing_price: String,
    val order_amount: String,
    val shipping_charge: String,
    val platform: String = "android"
)