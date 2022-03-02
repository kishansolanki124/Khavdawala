package com.app.khavdawala.pojo.response

data class ProductListResponse(
    var banner_list: ArrayList<Banner> = ArrayList(),
    var message: String = "",
    var products_list: ArrayList<Products> = ArrayList(),
    var status: String = "",
    var total_records: Int = 0
) {
    data class Banner(
        var banner_img: String = "",
        var category_id: String = ""
    )

    data class Products(
        var name: String = "",
        var isFav: Boolean = false,
        var selectedItemPosition: Int = 0,
        var packing_list: List<Packing> = listOf(),
        var price: String = "",
        var product_id: String = "",
        var up_pro_img: String = ""
    ) {
        data class Packing(
            var packing_id: String = "",
            var product_id: String = "",
            var product_price: String = "",
            var product_weight: String = "",
            var weight_type: String = ""
        )
    }
}