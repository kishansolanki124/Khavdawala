package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.response.SettingsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class SettingsViewModel : ViewModel() {

    private val mutableSettingsResponse = MutableLiveData<SettingsResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getSettings(user_mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    user_mobile
                )

                val apiResponse = apiEndPointsInterface.getSettings(
                    requestBodyBuilder.build()
                )
                returnSettingsResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnSettingsResponse(settingsResponse: SettingsResponse) {
        withContext(Dispatchers.Main) {
            mutableSettingsResponse.value = settingsResponse
        }
    }

    fun settingsResponse(): LiveData<SettingsResponse> {
        return mutableSettingsResponse
    }
}