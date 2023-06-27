package com.app.khavdawala.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.khavdawala.pojo.response.ProductListResponse

class SharedViewModel : ViewModel() {

    private val mutableLiveData: MutableLiveData<ProductListResponse.Products> = MutableLiveData()
    //if mutableEmptyCart is true then all the list should be updated to make empty cart
    private val mutableEmptyCart: MutableLiveData<Boolean> = MutableLiveData()

    fun setData(product: ProductListResponse.Products) {
        mutableLiveData.value = product
    }

    fun getData(): MutableLiveData<ProductListResponse.Products> = mutableLiveData

    fun setEmptyCart(product: Boolean) {
        mutableEmptyCart.value = true
    }

    fun cartIsEmpty(): MutableLiveData<Boolean> = mutableEmptyCart
}