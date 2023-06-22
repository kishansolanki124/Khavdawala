package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.khavdawala.apputils.AppConstants
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.response.ContactUsResponse
import com.app.khavdawala.pojo.response.NotificationResponse
import com.app.khavdawala.pojo.response.RegisterResponse
import com.app.khavdawala.pojo.response.StaticPageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class StaticPageViewModel : ViewModel() {

    private val mutableStaticPageResponse = MutableLiveData<StaticPageResponse>()
    private val mutableContactUsResponse = MutableLiveData<ContactUsResponse>()
    private val mutableCommonResponse = MutableLiveData<RegisterResponse>()
    private val mutableNotificationResponse = MutableLiveData<NotificationResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun inquiry(name: String, contact_no: String, email: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.NAME,
                    name
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.CONTACT_NO,
                    contact_no
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.MESSAGE,
                    message
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.EMAIL,
                    email
                )

                val apiResponse = apiEndPointsInterface.inquiry(
                    requestBodyBuilder.build()
                )
                returnCommonResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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

    fun getContactUs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = apiEndPointsInterface.getContactUs()
                returnContactUs(apiResponse)
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

    private suspend fun returnContactUs(settingsResponse: ContactUsResponse) {
        withContext(Dispatchers.Main) {
            mutableContactUsResponse.value = settingsResponse
        }
    }

    private suspend fun returnCommonResponse(settingsResponse: RegisterResponse) {
        withContext(Dispatchers.Main) {
            mutableCommonResponse.value = settingsResponse
        }
    }

    fun categoryResponse(): LiveData<StaticPageResponse> {
        return mutableStaticPageResponse
    }

    fun notificationResponse(): LiveData<NotificationResponse> {
        return mutableNotificationResponse
    }

    fun contactUs(): LiveData<ContactUsResponse> {
        return mutableContactUsResponse
    }

    fun commonResponse(): LiveData<RegisterResponse> {
        return mutableCommonResponse
    }
}