package com.app.khavdawala.pojo.response

data class NotificationResponse(
    var message: String = "",
    var notification_list: ArrayList<Notification> = ArrayList(),
    var banner_list: ArrayList<ProductListResponse.Banner> = ArrayList(),
    var status: String = ""
) {
    data class Notification(
        var description: String = "",
        var id: String = "",
        var pdate: String = "",
        var title: String = ""
    )
}