package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.request.ProductRequest
import com.app.khavdawala.pojo.response.CategoryResponse
import com.app.khavdawala.pojo.response.ProductListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class ProductViewModel : ViewModel() {

    private val mutableCategoryResponse = MutableLiveData<ProductListResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getProductList(productRequest: ProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.CID,
                    productRequest.cid.toString()
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.START,
                    productRequest.start.toString()
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.END,
                    productRequest.end.toString()
                )

                val apiResponse = apiEndPointsInterface.getProductList(requestBodyBuilder.build())
                returnCategoryResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnCategoryResponse(eMagazineResponse: ProductListResponse) {
        withContext(Dispatchers.Main) {
            mutableCategoryResponse.value = eMagazineResponse
        }
    }


    fun categoryResponse(): LiveData<ProductListResponse> {
        return mutableCategoryResponse
    }
}