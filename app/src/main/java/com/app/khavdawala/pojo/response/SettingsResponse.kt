package com.app.khavdawala.pojo.response

data class SettingsResponse(
    var settings: List<Setting> = listOf()
) {
    data class Setting(
        var android_version: String = "",
        var appsharemsg: String = "",
        var ios_version: String = "",
        var isfourceupdate: String = "",
        var update_link_android: String = "",
        var update_link_ios: String = "",
        var updatemsg: String = ""
    )
}