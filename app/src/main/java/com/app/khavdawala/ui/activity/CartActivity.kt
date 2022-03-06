package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.khavdawala.R
import com.app.khavdawala.apputils.SPreferenceManager
import com.app.khavdawala.apputils.getCartProductList
import com.app.khavdawala.databinding.ActivityCartBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.adapter.CartProductAdapter

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var govtWorkNewsAdapter: CartProductAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.rlCart.visibility = View.GONE
        binding.toolbar.ibBack.visibility = View.VISIBLE
        binding.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        layoutManager = LinearLayoutManager(this)
        binding.rvMLAs.layoutManager = layoutManager

        govtWorkNewsAdapter = CartProductAdapter(itemClick = {

        }, removeFromCartClick = { product, position ->
            removeFromCart(product)
            govtWorkNewsAdapter.itemRemovedFromCart(position)
        }, updateCartClick = { product, _ ->
            updateToCart(product)
            //categoryProductListAdapter.notifyItemChanged(position)
        })
        binding.rvMLAs.adapter = govtWorkNewsAdapter

        govtWorkNewsAdapter.reset()
        if (!getCartProductList().isNullOrEmpty()) {
            govtWorkNewsAdapter.setItem(getCartProductList())
        }

        binding.btCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }

        updateTotalCount()
    }

    private fun removeFromCart(product: ProductListResponse.Products) {
        var productList: ArrayList<ProductListResponse.Products>? =
            SPreferenceManager.getInstance(this)
                .getList("product", ProductListResponse.Products::class.java)

        if (productList.isNullOrEmpty()) {
            productList = ArrayList()
            //binding.toolbar.tvCartCount.text = productList.size.toString()
            SPreferenceManager.getInstance(this).putList("product", productList)
            return
        }

        for ((index, item) in productList.withIndex()) {
            if (item.product_id == product.product_id && item.cartPackingId == product.cartPackingId) {
                productList.removeAt(index)
                break
            }
        }
        //binding.toolbar.tvCartCount.text = productList.size.toString()
        SPreferenceManager.getInstance(this).putList("product", productList)

//        if (productList.isEmpty()) {
//            binding.toolbar.flCartCount.gone()
//        }
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
        //binding.toolbar.tvCartCount.text = productList.size.toString()
        SPreferenceManager.getInstance(this).putList("product", productList)

//        if (productList.isNotEmpty()) {
//            binding.toolbar.flCartCount.visible()
//        }
    }

    private fun updateTotalCount() {
        binding.tvTotalAmount.text = getString(R.string.total_rs, "100")
    }
}