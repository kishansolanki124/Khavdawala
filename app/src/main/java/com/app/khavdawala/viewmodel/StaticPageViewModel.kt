package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.response.NotificationResponse
import com.app.khavdawala.pojo.response.StaticPageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaticPageViewModel : ViewModel() {

    private val mutableStaticPageResponse = MutableLiveData<StaticPageResponse>()
    private val mutableNotificationResponse = MutableLiveData<NotificationResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getStaticPage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = apiEndPointsInterface.getStaticPage()
                returnCategoryResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = apiEndPointsInterface.getNotification()
                returnNotificationResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnCategoryResponse(eMagazineResponse: StaticPageResponse) {
        withContext(Dispatchers.Main) {
            mutableStaticPageResponse.value = eMagazineResponse
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnNotificationResponse(eMagazineResponse: NotificationResponse) {
        withContext(Dispatchers.Main) {
            mutableNotificationResponse.value = eMagazineResponse
        }
    }

    fun categoryResponse(): LiveData<StaticPageResponse> {
        return mutableStaticPageResponse
    }

    fun notificationResponse(): LiveData<NotificationResponse> {
        return mutableNotificationResponse
    }
}