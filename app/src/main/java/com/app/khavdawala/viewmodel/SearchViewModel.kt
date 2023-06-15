package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.khavdawala.apputils.AppConstants
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.request.ProductRequest
import com.app.khavdawala.pojo.response.ProductListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class SearchViewModel : ViewModel() {

    private val mutableCatWiseProductListModel = MutableLiveData<ProductListResponse>()
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
                    ""
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.SEARCH,
                    productRequest.search
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.START,
                    productRequest.start.toString()
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.END,
                    productRequest.end.toString()
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    productRequest.user_mobile
                )

                val apiResponse = apiEndPointsInterface.getProductList(requestBodyBuilder.build())
                returnProductList(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
//    fun searchProductsList(pageSize: Int, pageNo: Int, search: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//
//            try {
//                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
//
//                requestBodyBuilder.addFormDataPart(
//                    AppConstants.RequestParameters.PAGE_SIZE,
//                    pageSize.toString()
//                )
//
//                requestBodyBuilder.addFormDataPart(
//                    AppConstants.RequestParameters.PAGE_NO,
//                    pageNo.toString()
//                )
//                requestBodyBuilder.addFormDataPart(
//                    AppConstants.RequestParameters.SEARCH,
//                    search
//                )
//
//                val apiResponse =
//                    apiEndPointsInterface.searchProductList(requestBodyBuilder.build())
//                returnProductList(apiResponse)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
//    fun getProductsNextPage(pageSize: Int, pageNo: Int, search: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//
//                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
//
//                requestBodyBuilder.addFormDataPart(
//                    AppConstants.RequestParameters.PAGE_SIZE,
//                    pageSize.toString()
//                )
//
//                requestBodyBuilder.addFormDataPart(
//                    AppConstants.RequestParameters.PAGE_NO,
//                    pageNo.toString()
//                )
//                requestBodyBuilder.addFormDataPart(
//                    AppConstants.RequestParameters.SEARCH,
//                    search
//                )
//
//                val apiResponse =
//                    apiEndPointsInterface.searchProductList(requestBodyBuilder.build())
//                returnProductListNextPage(apiResponse)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }


    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnProductList(catWiseProductListModel: ProductListResponse) {
        withContext(Dispatchers.Main) {
            mutableCatWiseProductListModel.value = catWiseProductListModel
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
//    private suspend fun returnProductListNextPage(catWiseProductListModel: CatWiseProductListModel) {
//        withContext(Dispatchers.Main) {
//            mutableCatWiseProductListModel.value = catWiseProductListModel
//        }
//    }

    fun productListResponse(): LiveData<ProductListResponse> {
        return mutableCatWiseProductListModel
    }
}