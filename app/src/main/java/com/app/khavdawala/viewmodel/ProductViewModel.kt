package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.khavdawala.apputils.AppConstants
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.request.AddFavRequest
import com.app.khavdawala.pojo.request.FavProductRequest
import com.app.khavdawala.pojo.request.ProductRequest
import com.app.khavdawala.pojo.response.AddFavResponse
import com.app.khavdawala.pojo.response.GiftProductResponse
import com.app.khavdawala.pojo.response.ProductDetailResponse
import com.app.khavdawala.pojo.response.ProductListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class ProductViewModel : ViewModel() {

    private val mutableCategoryResponse = MutableLiveData<ProductListResponse>()
    private val mutableGiftProductResponse = MutableLiveData<GiftProductResponse>()
    private val mutableProductDetailResponse = MutableLiveData<ProductDetailResponse>()
    private val mutableAddFavResponse = MutableLiveData<AddFavResponse>()
    private val mutableRemoveFavResponse = MutableLiveData<AddFavResponse>()
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
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    productRequest.user_mobile
                )

                val apiResponse = apiEndPointsInterface.getProductList(requestBodyBuilder.build())
                returnCategoryResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getGiftProduct(productRequest: ProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

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

                val apiResponse = apiEndPointsInterface.getGiftProduct(requestBodyBuilder.build())
                returnGiftProductResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getProductDetail(productRequest: ProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.PID,
                    productRequest.pid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    productRequest.user_mobile
                )

                val apiResponse = apiEndPointsInterface.getProductDetail(requestBodyBuilder.build())
                returnProductDetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getFavProductList(productRequest: FavProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

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

                val apiResponse =
                    apiEndPointsInterface.getFavProductList(requestBodyBuilder.build())
                returnCategoryResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun addFavProduct(productRequest: AddFavRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    productRequest.user_mobile
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.PID,
                    productRequest.pid
                )

                val apiResponse = apiEndPointsInterface.addFavProduct(requestBodyBuilder.build())
                returnAddFavResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun removeFavProduct(productRequest: AddFavRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    productRequest.user_mobile
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.PID,
                    productRequest.pid
                )

                val apiResponse = apiEndPointsInterface.removeFavProduct(requestBodyBuilder.build())
                returnRemoveFavResponse(apiResponse)
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

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnGiftProductResponse(eMagazineResponse: GiftProductResponse) {
        withContext(Dispatchers.Main) {
            mutableGiftProductResponse.value = eMagazineResponse
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnProductDetailResponse(eMagazineResponse: ProductDetailResponse) {
        withContext(Dispatchers.Main) {
            mutableProductDetailResponse.value = eMagazineResponse
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnAddFavResponse(eMagazineResponse: AddFavResponse) {
        withContext(Dispatchers.Main) {
            mutableAddFavResponse.value = eMagazineResponse
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnRemoveFavResponse(eMagazineResponse: AddFavResponse) {
        withContext(Dispatchers.Main) {
            mutableRemoveFavResponse.value = eMagazineResponse
        }
    }

    fun categoryResponse(): LiveData<ProductListResponse> {
        return mutableCategoryResponse
    }

    fun giftProductResponse(): LiveData<GiftProductResponse> {
        return mutableGiftProductResponse
    }

    fun productDetailResponse(): LiveData<ProductDetailResponse> {
        return mutableProductDetailResponse
    }

    fun addFavResponse(): LiveData<AddFavResponse> {
        return mutableAddFavResponse
    }

    fun removeFavResponse(): LiveData<AddFavResponse> {
        return mutableRemoveFavResponse
    }
}