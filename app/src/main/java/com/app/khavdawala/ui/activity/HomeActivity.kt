package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.khavdawala.R
import com.app.khavdawala.apputils.SPreferenceManager
import com.app.khavdawala.apputils.getCartProductList
import com.app.khavdawala.apputils.gone
import com.app.khavdawala.apputils.visible
import com.app.khavdawala.databinding.ActivityHomeBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.fragment.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var mTransaction: FragmentTransaction
    //private lateinit var newsViewModel: NewsViewModel
    //private var newsCategoryList: ArrayList<NewsCategoryResponse.NewsCategory> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
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

        if (!getCartProductList().isNullOrEmpty()) {
            //todo work here
            binding.toolbar.tvCartCount.text = getCartProductList().size.toString()
            binding.toolbar.tvCartCount.visible()
        } else {
            binding.toolbar.tvCartCount.gone()
        }

        binding.toolbar.rlCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.bottomNavigationView.selectedItemId = R.id.navigation_home


//        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
//
//        newsViewModel.newsCategoryResponse().observe(this, Observer {
//            handleResponse(it)
//        })

        //fetchNewsCategories()
    }

//    private fun fetchNewsCategories() {
//        newsCategoryList = ArrayList()
//        if (isConnected(this)) {
//            showProgressDialog(this)
//            newsViewModel.newsCategory()
//        } else {
//            showSnackBar(getString(R.string.no_internet), this)
//        }
//
//    }

//    private fun handleResponse(newsCategoryResponse: NewsCategoryResponse?) {
//        dismissProgressDialog()
//        if (null != newsCategoryResponse) {
//            newsCategoryList.add(NewsCategoryResponse.NewsCategory("0", "All"))
//            newsCategoryList.addAll(newsCategoryResponse.news_category)
//
//            val divider = DividerItemDecoration(
//                rvNewsCategories.context,
//                DividerItemDecoration.VERTICAL
//            )
//            divider.setDrawable(
//                ContextCompat.getDrawable(baseContext, R.drawable.news_category_divider)!!
//            )
//
//            rvNewsCategories.addItemDecoration(divider)
//
//            rvNewsCategories.layoutManager = LinearLayoutManager(this)
//            val adapter = NewsCategoryAdapter {
//                (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NewsHomeFragment).fetchNews(
//                    true, NewsRequest(it.id!!.toInt(), 0, 10)
//                )
//                if (newsCategory.isVisible) {
//                    newsCategory.visibility = View.GONE
//                }
//            }
//            adapter.setItem(newsCategoryList)
//            rvNewsCategories.adapter = adapter
//        } else {
//            showSnackBar(getString(R.string.something_went_wrong), this)
//        }
//    }

//    private fun setupListener() {
//        binding.ivNavigation.setOnClickListener {
//            if (newsCategoryList.isNullOrEmpty()) {
//                return@setOnClickListener
//            }
//
//            if (newsCategory.isVisible) {
//                newsCategory.visibility = View.GONE
//            } else {
//                newsCategory.visibility = View.VISIBLE
//            }
//        }
//
////        newsCategory.setOnClickListener {
////            newsCategory.visibility = View.GONE
////        }
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        if (binding.bottomNavigationView.selectedItemId != item.itemId) {
//            when (item.itemId) {
//                R.id.navigation_gift -> {
//                    switchFragment(GiftListFragment(), false)
//                    binding.toolbar.ivShare.visibility = View.VISIBLE
//                    binding.toolbar.ibBack.visibility = View.GONE
//                }
//                R.id.navigation_fav -> {
//                    switchFragment(FavoriteListFragment(), false)
//                    binding.toolbar.ivShare.visibility = View.VISIBLE
//                    binding.toolbar.ibBack.visibility = View.GONE
//                }
//                R.id.navigation_home -> {
//                    switchFragment(HomeFragment(), false)
//                    binding.toolbar.ivShare.visibility = View.VISIBLE
//                    binding.toolbar.ibBack.visibility = View.GONE
//                }
//                R.id.navigation_not -> {
//                    switchFragment(NotificationListFragment(), false)
//                    binding.toolbar.ivShare.visibility = View.VISIBLE
//                    binding.toolbar.ibBack.visibility = View.GONE
//                }
//                R.id.navigation_about -> {
//                    switchFragment(AboutFragment(), false)
//                    binding.toolbar.ivShare.visibility = View.VISIBLE
//                    binding.toolbar.ibBack.visibility = View.GONE
//                }
//            }
//        }
//        return true
//    }

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
            if (item.product_id == product.product_id) {
                itemExistInCart = true
                break
            }
        }
        if (!itemExistInCart) {
            productList.add(product)
        }
        binding.toolbar.tvCartCount.text = productList.size.toString()
        SPreferenceManager.getInstance(this).putList("product", productList)
    }
}