package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.ActivityHomeBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.fragment.*
import com.app.khavdawala.viewmodel.SharedViewModel


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var mTransaction: FragmentTransaction
    private lateinit var sharedViewModelInstance: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModelInstance = ViewModelProvider(this)[SharedViewModel::class.java]

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_gift -> {
                    switchFragment(GiftListFragment(), false)
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
                R.id.navigation_fav -> {
                    switchFragment(FavoriteListFragment(), false)
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
                R.id.navigation_home -> {
                    switchFragment(HomeFragment(), false)
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
                R.id.navigation_not -> {
                    switchFragment(NotificationListFragment(), false)
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
                R.id.navigation_about -> {
                    switchFragment(AboutFragment(), false)
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
            }
            true // return true;
        }

        if (getCartProductList().isNotEmpty()) {
            binding.toolbar.tvCartCount.text = getCartProductList().size.toString()
            binding.toolbar.flCartCount.visible()
        } else {
            binding.toolbar.flCartCount.gone()
        }

        binding.toolbar.rlCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            cartLauncher.launch(intent)
        }

        binding.toolbar.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            searchLauncher.launch(intent)
        }

        binding.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    fun switchFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
        addInsteadOfReplace: Boolean = false
    ) {
        if (fragment is HomeFragment || fragment is CategoryProductListFragment || fragment is ProductDetailFragment) {
            binding.ivHome.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.home_icon_active
                )
            )
        } else {
            binding.ivHome.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.home_icon))
        }

        mTransaction = supportFragmentManager.beginTransaction()

        if (addInsteadOfReplace) {
            mTransaction.add(R.id.fragmentContainer, fragment)
        } else {
            mTransaction.replace(R.id.fragmentContainer, fragment)
        }


        if (addToBackStack) {
            mTransaction.addToBackStack(null)
        }
        mTransaction.commit()

        binding.toolbar.ibBack.visibility = View.VISIBLE
        binding.toolbar.ivShare.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()

        supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let {
            // the fragment exists
            when (it) {
                is FavoriteListFragment -> {
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
                is HomeFragment -> {
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
                is NotificationListFragment -> {
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
                is AboutFragment -> {
                    binding.toolbar.ivShare.visibility = View.VISIBLE
                    binding.toolbar.ibBack.visibility = View.GONE
                }
                else -> {
                    binding.toolbar.ivShare.visibility = View.GONE
                    binding.toolbar.ibBack.visibility = View.VISIBLE
                }
            }
        }
    }

    fun addToCart(product: ProductListResponse.Products) {
        var productList: ArrayList<ProductListResponse.Products>? =
            SPreferenceManager.getInstance(this)
                .getList("product", ProductListResponse.Products::class.java)

        if (productList.isNullOrEmpty()) {
            productList = ArrayList()
        }

        var itemExistInCart = false
        for (item in productList) {
            if (item.product_id == product.product_id && item.cartPackingId == product.cartPackingId) {
                itemExistInCart = true
                break
            }
        }
        if (!itemExistInCart) {
            productList.add(product)
        }
        binding.toolbar.tvCartCount.text = productList.size.toString()
        SPreferenceManager.getInstance(this).putList("product", productList)

        if (productList.isNotEmpty()) {
            binding.toolbar.flCartCount.visible()
        }
    }

    fun updateToCart(product: ProductListResponse.Products) {
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
        binding.toolbar.tvCartCount.text = productList.size.toString()
        SPreferenceManager.getInstance(this).putList("product", productList)

        if (productList.isNotEmpty()) {
            binding.toolbar.flCartCount.visible()
        }
    }

    fun removeFromCart(product: ProductListResponse.Products) {
        var productList: ArrayList<ProductListResponse.Products>? =
            SPreferenceManager.getInstance(this)
                .getList("product", ProductListResponse.Products::class.java)

        if (productList.isNullOrEmpty()) {
            productList = ArrayList()
            binding.toolbar.tvCartCount.text = productList.size.toString()
            SPreferenceManager.getInstance(this).putList("product", productList)
            return
        }

        for ((index, item) in productList.withIndex()) {
            if (item.product_id == product.product_id && item.cartPackingId == product.cartPackingId) {
                productList.removeAt(index)
                break
            }
        }
        binding.toolbar.tvCartCount.text = productList.size.toString()
        SPreferenceManager.getInstance(this).putList("product", productList)

        if (productList.isEmpty()) {
            binding.toolbar.flCartCount.gone()
        }
    }

    private var searchLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppConstants.RequestCode.SEARCH_ACTIVITY) {
                val data: Intent? = result.data
                if (null != data) {
                    switchFragment(
                        ProductDetailFragment.newInstance(data.getStringExtra(AppConstants.SEARCH_STRING)!!),
                        addToBackStack = true, addInsteadOfReplace = true
                    )
                }
            }
        }

    private var cartLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppConstants.RequestCode.CART_ACTIVITY) {
                if (!getCartProductList().isNullOrEmpty()) {
                    for (item in getCartProductList()) {
                        sharedViewModelInstance.setData(item)
                    }
                } else {
                    sharedViewModelInstance.setEmptyCart(true)
                }
            }
        }

    override fun onResume() {
        super.onResume()
        if (!getCartProductList().isNullOrEmpty()) {
            binding.toolbar.tvCartCount.text = getCartProductList().size.toString()
            binding.toolbar.flCartCount.visible()
        } else {
            binding.toolbar.flCartCount.gone()
        }
    }

}