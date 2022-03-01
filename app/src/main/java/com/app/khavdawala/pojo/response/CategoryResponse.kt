package com.app.khavdawala.pojo.response

data class CategoryResponse(
    var category_list: ArrayList<Category> = ArrayList(),
    var message: String = "",
    var slider_list: ArrayList<Slider> = ArrayList(),
    var status: String = ""
) {
    data class Category(
        var id: String = "",
        var name: String = "",
        var up_pro_img: String = ""
    )

    data class Slider(
        var id: String = "",
        var up_pro_img: String = ""
    )
}