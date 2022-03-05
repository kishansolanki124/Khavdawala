package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.khavdawala.R
import com.app.khavdawala.apputils.gone
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.apputils.visible
import com.app.khavdawala.databinding.FragmentHomeBinding
import com.app.khavdawala.pojo.response.CategoryResponse
import com.app.khavdawala.ui.activity.HomeActivity
import com.app.khavdawala.ui.adapter.IntroAdapter
import com.app.khavdawala.ui.adapter.ProductCategoryAdapter
import com.app.khavdawala.viewmodel.CategoryViewModel

class HomeFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: ProductCategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.ivNext.setOnClickListener {
            binding.vpCategory.currentItem = binding.vpCategory.currentItem + 1
        }

        binding.ivPrev.setOnClickListener {
            binding.vpCategory.currentItem = binding.vpCategory.currentItem - 1
        }

        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        categoryViewModel.categoryResponse().observe(requireActivity()) {
            handleResponse(it)
        }

        fetchMagazineList()
    }

    private fun initRecyclerView() {
        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCategory.layoutManager = layoutManager

        govtWorkNewsAdapter = ProductCategoryAdapter {
            (requireActivity() as HomeActivity).switchFragment(
                CategoryProductListFragment.newInstance(
                    it.id.toInt()
                ), true
            )
        }
        binding.rvCategory.adapter = govtWorkNewsAdapter
    }

    private fun setupHorizontalMainNews(scrollNewsList: List<CategoryResponse.Slider>) {
        val adapter = IntroAdapter {
//            startActivity(
//                Intent(
//                    requireActivity(), NewsDetailsActivity::class.java
//                ).putExtra(AppConstants.NEWS_ID, it.id)
//            )
        }
        adapter.setItem(scrollNewsList)
        binding.vpCategory.adapter = adapter

//        TabLayoutMediator(binding.introTabLayout, newsHomeViewPager) { tab, position ->
//            println("selected tab is $tab and position is $position")
//        }.attach()

//        val handler = Handler(Looper.myLooper()!!)
//        var currentPage = 0
//        val update = Runnable {
//            if (currentPage == scrollNewsList.size) {
//                currentPage = 0
//            }
//
//            if (null != newsHomeViewPager) {
//                newsHomeViewPager.setCurrentItem(currentPage++, true)
//            }
//        }
//
//        timer = Timer()
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(update)
//            }
//        }, 4000, 4000)
    }

    private fun fetchMagazineList() {
        if (isConnected(requireContext())) {
            binding.rvCategory.gone()
            binding.loading.pbCommon.visible()
            categoryViewModel.getCategories()
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun handleResponse(eMagazineResponse: CategoryResponse?) {
        if (null != eMagazineResponse) {
            if (eMagazineResponse.category_list.isNotEmpty()) {
                setupCategoryList(eMagazineResponse)
                if (eMagazineResponse.slider_list.isNotEmpty()) {
                    setupHorizontalMainNews(eMagazineResponse.slider_list)
                }
            } else {
                showSnackBar(eMagazineResponse.message, requireActivity())
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
        binding.rvCategory.visible()
        binding.loading.pbCommon.gone()
    }

    private fun setupCategoryList(categoryResponse: CategoryResponse) {
        govtWorkNewsAdapter.reset()
        govtWorkNewsAdapter.setItem(categoryResponse.category_list)
    }
}