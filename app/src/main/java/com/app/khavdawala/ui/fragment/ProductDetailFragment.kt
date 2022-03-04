package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.R
import com.app.khavdawala.apputils.gone
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.apputils.visible
import com.app.khavdawala.databinding.FragmentProductDetailBinding
import com.app.khavdawala.pojo.request.ProductRequest
import com.app.khavdawala.pojo.response.ProductDetailResponse
import com.app.khavdawala.ui.activity.HomeActivity
import com.app.khavdawala.ui.adapter.TabFragmentAdapter
import com.app.khavdawala.ui.adapter.HorizontalProductListAdapter
import com.app.khavdawala.ui.adapter.ProductDetailImagesAdapter
import com.app.khavdawala.viewmodel.ProductViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ProductDetailFragment : Fragment() {

    private lateinit var tabFragmentAdapter: TabFragmentAdapter
    private lateinit var govtWorkNewsAdapter: HorizontalProductListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentProductDetailBinding
    private var pid = ""
    private lateinit var categoryViewModel: ProductViewModel
    private var fragmentList: ArrayList<Fragment> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        if (isConnected(requireContext())) {
            binding.pbHome.visible()
            binding.newsHomeViewPager.gone()
            binding.rlImage.gone()
            binding.tvSweetName.gone()
            binding.clButtons.gone()
            binding.tabLayout.gone()
            binding.pager.gone()
            binding.tvYouMayLike.gone()
            binding.rvProduct.gone()
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
        categoryViewModel.getProductDetail(ProductRequest(pid = pid))

        categoryViewModel.productDetailResponse().observe(requireActivity()) {
            handleResponse(it)
        }
    }

    private fun handleResponse(productDetailResponse: ProductDetailResponse?) {
        binding.pbHome.gone()
        binding.newsHomeViewPager.visible()
        binding.rlImage.visible()
        binding.tvSweetName.visible()
        binding.clButtons.visible()
        binding.tabLayout.visible()
        binding.pager.visible()
        binding.tvYouMayLike.visible()
        binding.rvProduct.visible()

        if (null != productDetailResponse) {
            if (productDetailResponse.status == "1") {
                setupHorizontalMainNews(productDetailResponse.product_gallery)
                setupViews(productDetailResponse)
                setupList(productDetailResponse.youmay_alsolike)
            } else {
                showSnackBar(productDetailResponse.message, requireActivity())
            }
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun setupViews(productDetailResponse: ProductDetailResponse) {
        binding.tvSweetName.text = productDetailResponse.product_detail[0].name
        setupSpinner(productDetailResponse.product_packing)
        //if(binding.ivFavIcon)todo work here

        fragmentList.clear()
        fragmentList.add(ProductDescriptionFragment.newInstance(productDetailResponse.product_detail[0].description))
        fragmentList.add(ProductDescriptionFragment.newInstance((productDetailResponse.product_detail[0].nutrition)))

        tabFragmentAdapter = TabFragmentAdapter(this, fragmentList, 2)
        binding.pager.adapter = tabFragmentAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Description"
                }
                else -> {
                    tab.text = "Nutrition Value"
                }

            }

        }.attach()
    }

    private fun setupSpinner(productPacking: List<ProductDetailResponse.ProductPacking>) {
        val adapter: ArrayAdapter<ProductDetailResponse.ProductPacking> = ArrayAdapter(
            binding.spStateGujaratiSamaj.context,
            R.layout.spinner_display_item,
            productPacking
        )

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        binding.spStateGujaratiSamaj.adapter = adapter

        binding.spStateGujaratiSamaj.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
//                    stateId = stateList[p2].id!!
//                    filterCitySpinnerList(stateId)

//                        if (selectionCount++ > 1) {
//                            //onItemSelected(p2)
//                            //newsPortal.let { it1 -> itemClickWeb.invoke(it1) }
//                        }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    return
                }
            }
    }

    private fun setupList(productList: ArrayList<ProductDetailResponse.YoumayAlsolike>) {
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvProduct.layoutManager = layoutManager

        govtWorkNewsAdapter = HorizontalProductListAdapter {
            (requireActivity() as HomeActivity).switchFragment(ProductDetailFragment(), false)
        }
        binding.rvProduct.adapter = govtWorkNewsAdapter
        govtWorkNewsAdapter.reset()
        govtWorkNewsAdapter.setItem(productList)
    }

    private fun setupHorizontalMainNews(scrollNewsList: List<ProductDetailResponse.ProductGallery>) {
        val adapter = ProductDetailImagesAdapter {
//            startActivity(
//                Intent(
//                    requireActivity(), NewsDetailsActivity::class.java
//                ).putExtra(AppConstants.NEWS_ID, it.id)
//            )
        }
        adapter.setItem(scrollNewsList)
        binding.newsHomeViewPager.adapter = adapter

        TabLayoutMediator(binding.introTabLayout, binding.newsHomeViewPager) { tab, position ->
            println("selected tab is $tab and position is $position")
        }.attach()

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

    companion object {
        fun newInstance(pid: String): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            fragment.pid = pid
            return fragment
        }
    }
}