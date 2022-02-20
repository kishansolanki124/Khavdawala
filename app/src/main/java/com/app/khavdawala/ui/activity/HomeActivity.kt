package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.khavdawala.R
import com.app.khavdawala.databinding.ActivityHomeBinding
import com.app.khavdawala.ui.fragment.AboutFragment
import com.app.khavdawala.ui.fragment.FavoriteListFragment
import com.app.khavdawala.ui.fragment.HomeFragment
import com.app.khavdawala.ui.fragment.NotificationListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var mTransaction: FragmentTransaction
    //private lateinit var newsViewModel: NewsViewModel
    //private var newsCategoryList: ArrayList<NewsCategoryResponse.NewsCategory> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)

        binding.toolbar.ivCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (binding.bottomNavigationView.selectedItemId != item.itemId) {
            when (item.itemId) {
                R.id.navigation_gift -> {
                    //switchFragment(HomeFragment(), false)
                }
                R.id.navigation_fav -> {
                    switchFragment(FavoriteListFragment(), false)
                }
                R.id.navigation_home -> {
                    switchFragment(HomeFragment(), false)
                }
                R.id.navigation_not -> {
                    switchFragment(NotificationListFragment(), false)
                }
                R.id.navigation_about -> {
                    switchFragment(AboutFragment(), false)
                }
            }
        }
        return true
    }

    fun switchFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (fragment is HomeFragment) {
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
        mTransaction.replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) {
            mTransaction.addToBackStack(null)
        }
        mTransaction.commit()
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//
////        supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let {
////            // the fragment exists
////            when (it) {
//
////        when (supportFragmentManager.fragments.last()) {
////            is NewsHomeFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_news
////            }
////            is MenuFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_menu
////            }
////            is ShraddhanjaliHomeFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_shraddhanjali
////            }
////            is EMagazineFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_magazine
////            }
////            is OpinionPollFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_opinion_poll
////            }
////        }
//    }
}