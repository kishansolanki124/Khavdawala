package com.app.khavdawala.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.khavdawala.network.APIEndPointsInterface
import com.app.khavdawala.network.RetrofitFactory
import com.app.khavdawala.pojo.response.CategoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel : ViewModel() {

    private val mutableCategoryResponse = MutableLiveData<CategoryResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = apiEndPointsInterface.getCategories()
                returnCategoryResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.Main for UI related stuff which runs on Main thread
     */
    private suspend fun returnCategoryResponse(eMagazineResponse: CategoryResponse) {
        withContext(Dispatchers.Main) {
            mutableCategoryResponse.value = eMagazineResponse
        }
    }


    fun categoryResponse(): LiveData<CategoryResponse> {
        return mutableCategoryResponse
    }
}