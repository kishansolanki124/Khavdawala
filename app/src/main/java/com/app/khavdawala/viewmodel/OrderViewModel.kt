package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.request.OrderPlaceRequest
import com.app.khavdawala.pojo.response.OrderAddressResponse
import com.app.khavdawala.pojo.response.RegisterResponse
import com.app.khavdawala.pojo.response.ShippingChargeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class OrderViewModel : ViewModel() {

    private val mutableSignupResponseModel = MutableLiveData<RegisterResponse>()
    private val mutableAddressResponse = MutableLiveData<OrderAddressResponse>()
    private val mutableShippingChargeResponse = MutableLiveData<ShippingChargeResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun addOrder(registerRequest: OrderPlaceRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.customer_name,
                    registerRequest.customer_name
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.customer_email,
                    registerRequest.customer_email
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.area,
                    registerRequest.area
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.sub_area,
                    registerRequest.sub_area
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.address,
                    registerRequest.address
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.zipcode,
                    registerRequest.zipcode
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.city,
                    registerRequest.city
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.state,
                    registerRequest.state
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.mobile_no,
                    registerRequest.mobile_no
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.alternate_contact_no,
                    registerRequest.alternate_contact_no
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.product_id,
                    registerRequest.product_id
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.product_name,
                    registerRequest.product_name
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.packing_id,
                    registerRequest.packing_id
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.packing_weight,
                    registerRequest.packing_weight
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.packing_weight_type,
                    registerRequest.packing_weight_type
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.packing_quantity,
                    registerRequest.packing_quantity
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.packing_price,
                    registerRequest.packing_price
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.order_amount,
                    registerRequest.order_amount
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.shipping_charge,
                    registerRequest.shipping_charge
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.platform,
                    registerRequest.platform
                )

                val apiResponse = apiEndPointsInterface.addOrder(requestBodyBuilder.build())
                returnSignupResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getShippingCharge() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = apiEndPointsInterface.getShippingCharge()
                returnShippingCharge(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getAddress(mobileNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.mobile_no,
                    mobileNo
                )
                val apiResponse = apiEndPointsInterface.getAddress(requestBodyBuilder.build())
                returnAddressResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnAddressResponse(apiResponse: OrderAddressResponse) {
        withContext(Dispatchers.Main) {
            mutableAddressResponse.value = apiResponse
        }
    }

    private suspend fun returnShippingCharge(shippingChargeResponse: ShippingChargeResponse) {
        withContext(Dispatchers.Main) {
            mutableShippingChargeResponse.value = shippingChargeResponse
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

    fun registerResponse(): LiveData<RegisterResponse> {
        return mutableSignupResponseModel
    }

    fun shippingChargeResponse(): LiveData<ShippingChargeResponse> {
        return mutableShippingChargeResponse
    }

    fun addressResponse(): LiveData<OrderAddressResponse> {
        return mutableAddressResponse
    }
}