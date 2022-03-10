package com.app.khavdawala.pojo.response

data class ShippingChargeResponse(
    var message: String = "",
    var shipping_charge: List<ShippingCharge> = listOf(),
    var status: String = ""
) {
    data class ShippingCharge(
        var gujarat_active: String = "",
        var gujarat_shipping: String = "",
        var id: String = "",
        var outof_gujarat_shipping: String = "",
        var outofgujarat_active: String = "",
        var rajkot_min_amount: String = "",
        var rajkot_shipping: String = ""
    )
}