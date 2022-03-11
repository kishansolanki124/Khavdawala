package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.khavdawala.R
import com.app.khavdawala.apputils.SPreferenceManager
import com.app.khavdawala.apputils.getCartProductList
import com.app.khavdawala.apputils.gone
import com.app.khavdawala.apputils.visible
import com.app.khavdawala.databinding.ActivityCartBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.adapter.CartProductAdapter

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var govtWorkNewsAdapter: CartProductAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var totalAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.rlCart.visibility = View.GONE
        binding.toolbar.ibBack.visibility = View.VISIBLE
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
            startActivity(Intent(this, CheckoutActivity::class.java)
                .putExtra(AppConstants.AMOUNT, totalAmount))
            finish()
        }

        updateTotalCount()
    }

    private fun showAlertToClearCart() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure want to clear your Cart? All the items in your cart will be removed.")
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            getString(R.string.Clear_Cart)
        ) { dialog, _ ->
            dialog.dismiss()
            clearCart()
        }

        alertDialogBuilder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.btnBG))
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.gray_hint))
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
            binding.tvTotalAmount.text = getString(R.string.total_rs, totalAmount.toString())
            binding.rlCart.visible()
            binding.rlEmpty.gone()
        } else {
            binding.tvTotalAmount.text = getString(R.string.total_rs, "0")
            binding.rlEmpty.visible()
            binding.rlCart.gone()
        }
    }
}