package com.app.khavdawala.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.AppConstants
import com.app.khavdawala.apputils.SPreferenceManager
import com.app.khavdawala.apputils.getCartProductList
import com.app.khavdawala.apputils.gone
import com.app.khavdawala.apputils.shareApp
import com.app.khavdawala.apputils.visible
import com.app.khavdawala.databinding.ActivityHomeBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.fragment.AboutFragment
import com.app.khavdawala.ui.fragment.CategoryProductListFragment
import com.app.khavdawala.ui.fragment.FavoriteListFragment
import com.app.khavdawala.ui.fragment.GiftListFragment
import com.app.khavdawala.ui.fragment.HomeFragment
import com.app.khavdawala.ui.fragment.NotificationListFragment
import com.app.khavdawala.ui.fragment.ProductDetailFragment
import com.app.khavdawala.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var mTransaction: FragmentTransaction
    private lateinit var sharedViewModelInstance: SharedViewModel
    private var openNotificationFragment = false

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        }
//                        else {
//                            showSettingDialog()
//                        }
                    }
                }
            } else {
//                Toast.makeText(applicationContext, "notification permission granted", Toast.LENGTH_SHORT)
//                    .show()
            }
        }

//    private fun showSettingDialog() {
//        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
//            .setTitle("Notification Permission")
//            .setMessage("Notification permission is required, Please allow notification permission from setting")
//            .setPositiveButton("Ok") { _, _ ->
//                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.data = Uri.parse("package:$packageName")
//                startActivity(intent)
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    var hasNotificationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            openNotificationFragment = intent.getBooleanExtra("Notification", false)
        }

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

        binding.toolbar.ivShare.setOnClickListener {
            shareApp()
        }

        binding.toolbar.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            searchLauncher.launch(intent)
        }

        binding.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        if (openNotificationFragment) {
            binding.bottomNavigationView.selectedItemId = R.id.navigation_not
        } else {
            binding.bottomNavigationView.selectedItemId = R.id.navigation_home
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
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
                if (getCartProductList().isNotEmpty()) {
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
        if (getCartProductList().isNotEmpty()) {
            binding.toolbar.tvCartCount.text = getCartProductList().size.toString()
            binding.toolbar.flCartCount.visible()
        } else {
            binding.toolbar.flCartCount.gone()
        }
    }
}