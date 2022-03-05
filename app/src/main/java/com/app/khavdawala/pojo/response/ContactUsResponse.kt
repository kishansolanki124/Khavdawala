package com.app.khavdawala.pojo.response

data class ContactUsResponse(
    var contact_us: List<ContactU> = listOf(),
    var message: String = "",
    var status: String = ""
) {
    data class ContactU(
        var address: String = "",
        var email: String = "",
        var google_map: String = "",
        var name: String = "",
        var phone: String = ""
    )
}