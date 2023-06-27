package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.khavdawala.apputils.AppConstants
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.response.RegisterResponse
import com.app.khavdawala.pojo.request.RegisterRequest
import com.app.khavdawala.pojo.response.CheckUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class RegisterViewModel : ViewModel() {

    private val mutableSignupResponseModel = MutableLiveData<RegisterResponse>()
    private val mutableCheckUserResponseModel = MutableLiveData<CheckUserResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun registerUser(registerRequest: RegisterRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.NAME,
                    registerRequest.name
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.MOBILE,
                    registerRequest.mobile
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.BIRTH_DATE,
                    registerRequest.birth_date
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.device_id,
                    registerRequest.deviceId
                )

                val apiResponse = apiEndPointsInterface.registration(requestBodyBuilder.build())
                returnSignupResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun checkUser(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.device_id,
                    deviceId
                )

                val apiResponse = apiEndPointsInterface.checkUser(requestBodyBuilder.build())
                returnCheckUserResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnSignupResponse(registerResponse: RegisterResponse) {
        withContext(Dispatchers.Main) {
            mutableSignupResponseModel.value = registerResponse
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnCheckUserResponse(registerResponse: CheckUserResponse) {
        withContext(Dispatchers.Main) {
            mutableCheckUserResponseModel.value = registerResponse
        }
    }

    fun registerResponse(): LiveData<RegisterResponse> {
        return mutableSignupResponseModel
    }

    fun checkUserResponse(): LiveData<CheckUserResponse> {
        return mutableCheckUserResponseModel
    }

}