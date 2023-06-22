package com.app.khavdawala.pojo.response

data class StaticPageResponse(
    var message: String = "",
    var staticpage: List<Staticpage> = listOf(),
    var banner_list: ArrayList<ProductListResponse.Banner> = ArrayList(),
    var status: String = ""
) {
    data class Staticpage(
        var description: String = "",
        var name: String = ""
    )
}