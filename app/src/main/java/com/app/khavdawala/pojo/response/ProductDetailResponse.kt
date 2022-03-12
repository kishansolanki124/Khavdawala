package com.app.khavdawala.pojo.response

import java.io.Serializable

data class ProductDetailResponse(
    var message: String = "",
    var product_detail: List<ProductDetail> = listOf(),
    var product_gallery: List<ProductGallery> = listOf(),
    var product_packing: List<ProductPacking> = listOf(),
    var status: String = "",
    var youmay_alsolike: ArrayList<YoumayAlsolike> = ArrayList()
) {
    data class ProductDetail(
        var description: String? = null,
        var name: String = "",
        var favourite: String = "",
        var nutrition: String? = null,
        var price: String = "",
        var product_id: String = ""
    )

    data class ProductGallery(
        var id: String = "",
        var product_id: String = "",
        var up_pro_img: String = ""
    ) : Serializable

    data class ProductPacking(
        var packing_id: String = "",
        var product_id: String = "",
        var product_price: String = "",
        var product_weight: String = "",
        var weight_type: String = ""
    ) {
        override fun toString(): String {
            return "Rs. $product_price ($product_weight$weight_type)"
        }
    }

    data class YoumayAlsolike(
        var name: String = "",
        var product_id: String = "",
        var up_pro_img: String = ""
    )
}