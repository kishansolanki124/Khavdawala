package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.ActivityCartBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.pojo.response.ShippingChargeResponse
import com.app.khavdawala.ui.adapter.CartProductAdapter
import com.app.khavdawala.viewmodel.OrderViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var govtWorkNewsAdapter: CartProductAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var totalAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvHeader.text = getString(R.string.Cart)
        binding.toolbar.ivSearch.gone()
        binding.toolbar.rlCart.gone()
        binding.toolbar.ibBack.visible()
        binding.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.btClearCart.setOnClickListener {
            showAlertToClearCart()
        }

        layoutManager = LinearLayoutManager(this)
        binding.rvMLAs.layoutManager = layoutManager

        govtWorkNewsAdapter = CartProductAdapter(itemClick = {

        }, removeFromCartClick = { product, position ->
            removeFromCart(product)
            govtWorkNewsAdapter.itemRemovedFromCart(position)
            updateTotalCount()
        }, updateCartClick = { product, _ ->
            updateToCart(product)
            updateTotalCount()
        })
        binding.rvMLAs.adapter = govtWorkNewsAdapter

        govtWorkNewsAdapter.reset()
        if (!getCartProductList().isNullOrEmpty()) {
            govtWorkNewsAdapter.setItem(getCartProductList())
        }

        binding.btCheckout.setOnClickListener {
            startActivity(
                Intent(this, CheckoutActivity::class.java)
                    .putExtra(AppConstants.AMOUNT, totalAmount)
            )
            //finish()
        }

        updateTotalCount()

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        if (isConnected(this)) {
            orderViewModel.getShippingCharge()
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }

        orderViewModel.shippingChargeResponse().observe(this) {
            handleShippingChargeResponse(it)
        }
    }

    private fun showAlertToClearCart() {
        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
            .setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.Clear_Cart_msg))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.Clear_Cart)) { dialog, _ ->
                dialog.dismiss()
                clearCart()
            }
            .setNegativeButton(resources.getString(android.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun clearCart() {
        var productList: ArrayList<ProductListResponse.Products>? =
            SPreferenceManager.getInstance(this)
                .getList("product", ProductListResponse.Products::class.java)

        if (productList.isNullOrEmpty()) {
            productList = ArrayList()
            SPreferenceManager.getInstance(this).putList("product", productList)
        } else {
            productList.clear()
            SPreferenceManager.getInstance(this).putList("product", productList)
        }
        binding.rlEmpty.visible()
        binding.rlCart.gone()
    }

    private fun removeFromCart(product: ProductListResponse.Products) {
        var productList: ArrayList<ProductListResponse.Products>? =
            SPreferenceManager.getInstance(this)
                .getList("product", ProductListResponse.Products::class.java)

        if (productList.isNullOrEmpty()) {
            productList = ArrayList()
            SPreferenceManager.getInstance(this).putList("product", productList)
            return
        }

        for ((index, item) in productList.withIndex()) {
            if (item.product_id == product.product_id && item.cartPackingId == product.cartPackingId) {
                productList.removeAt(index)
                break
            }
        }
        SPreferenceManager.getInstance(this).putList("product", productList)
    }

    private fun updateToCart(product: ProductListResponse.Products) {
        var productList: ArrayList<ProductListResponse.Products>? =
            SPreferenceManager.getInstance(this)
                .getList("product", ProductListResponse.Products::class.java)

        if (productList.isNullOrEmpty()) {
            productList = ArrayList()
        }

        var itemExistInCart = false
        for ((index, item) in productList.withIndex()) {
            if (item.product_id == product.product_id && item.cartPackingId == product.cartPackingId) {
                itemExistInCart = true
                productList[index].itemQuantity = product.itemQuantity
                break
            }
        }
        if (!itemExistInCart) {
            productList.add(product)
        }
        SPreferenceManager.getInstance(this).putList("product", productList)
    }

    private fun updateTotalCount() {
        if (!getCartProductList().isNullOrEmpty()) {
            val cartProductList = getCartProductList()
            totalAmount = 0.0
            for (item in cartProductList) {
                totalAmount += (item.packing_list[item.selectedItemPosition].product_price.toDouble() * item.itemQuantity)
            }
            binding.tvTotalAmount.text = rupeesWithTwoDecimal(totalAmount)
            binding.rlCart.visible()
            binding.rlEmpty.gone()
        } else {
            binding.tvTotalAmount.text = rupeesWithTwoDecimal(0.0)
            binding.rlEmpty.visible()
            binding.rlCart.gone()
        }
    }

    private fun handleShippingChargeResponse(shippingChargeResponse: ShippingChargeResponse?) {
        if (null != shippingChargeResponse) {
            setupHorizontalMainNews(shippingChargeResponse.banner_list)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun setupHorizontalMainNews(bannerList: java.util.ArrayList<ProductListResponse.Banner>) {
        if (bannerList.isNotEmpty()) {
            for (item in bannerList) {
                if (item.banner_name == "Cart") {
                    binding.ivCategoryHeader.loadImage(item.banner_img)
                    break
                }
            }
        }
    }
}