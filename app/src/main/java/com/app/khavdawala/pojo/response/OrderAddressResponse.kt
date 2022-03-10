package com.app.khavdawala.pojo.response

data class OrderAddressResponse(
    var address_detail: List<AddressDetail> = listOf(),
    var message: String = "",
    var status: String = ""
) {
    data class AddressDetail(
        var alternate_contact_no: String = "",
        var area: String = "",
        var city: String = "",
        var customer_email: String = "",
        var customer_name: String = "",
        var mobile_no: String = "",
        var sub_area: String = "",
        var zipcode: String = ""
    )
}