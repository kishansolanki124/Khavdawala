package com.app.khavdawala.ui.fragment

import android.graphics.Color
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
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.FragmentProductDetailBinding
import com.app.khavdawala.pojo.request.AddFavRequest
import com.app.khavdawala.pojo.request.ProductRequest
import com.app.khavdawala.pojo.response.AddFavResponse
import com.app.khavdawala.pojo.response.ProductDetailResponse
import com.app.khavdawala.ui.activity.HomeActivity
import com.app.khavdawala.ui.adapter.HorizontalProductListAdapter
import com.app.khavdawala.ui.adapter.ProductDetailImagesAdapter
import com.app.khavdawala.viewmodel.ProductViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductDetailFragment : Fragment() {

    //private lateinit var tabFragmentAdapter: TabFragmentAdapter
    private lateinit var govtWorkNewsAdapter: HorizontalProductListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var productLiked = false
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

        binding.ivFavIcon.setOnClickListener {
            if (isConnected(requireContext())) {
                binding.pbFav.visible()
                binding.ivFavIcon.invisible()
                if (productLiked) {
                    removeFavProduct()
                } else {
                    callAddToFav()
                }
            } else {
                showSnackBar(getString(R.string.no_internet), requireActivity())
            }
        }

        binding.tvMinus.setOnClickListener {
            var currentProductCount = binding.tvProductCount.text.toString().toInt()
            if (currentProductCount != 0) {
                currentProductCount -= 1
            }
            binding.tvProductCount.text = currentProductCount.toString()
        }

        binding.tvPlus.setOnClickListener {
            var currentProductCount = binding.tvProductCount.text.toString().toInt()
            if (currentProductCount != 99) {
                currentProductCount += 1
            }
            binding.tvProductCount.text = currentProductCount.toString()
        }

        categoryViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        categoryViewModel.addFavResponse().observe(requireActivity()) {
            handleFavResponse(it)
        }

        categoryViewModel.removeFavResponse().observe(requireActivity()) {
            handleRemoveFavResponse(it)
        }

        if (isConnected(requireContext())) {
            binding.loading.pbCommon.visible()
            binding.tvSweetName.gone()
            binding.clButtons.gone()
            binding.tabLayout.gone()
            //binding.vpProductDescNut.gone()
            binding.tvYouMayLike.gone()
            binding.rvProduct.gone()
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
        categoryViewModel.getProductDetail(
            ProductRequest(
                pid = pid,
                user_mobile = SPreferenceManager.getInstance(requireContext()).session
            )
        )

        categoryViewModel.productDetailResponse().observe(requireActivity()) {
            handleResponse(it)
        }
    }

    private fun handleResponse(productDetailResponse: ProductDetailResponse?) {
        binding.loading.pbCommon.gone()
        binding.tvSweetName.visible()
        binding.clButtons.visible()
        binding.tabLayout.visible()
        //binding.vpProductDescNut.visible()
        binding.tvYouMayLike.visible()
        binding.rvProduct.visible()

        if (null != productDetailResponse) {
            if (productDetailResponse.status == "1") {
                setupHorizontalMainNews(productDetailResponse.product_gallery)
                setupViews(productDetailResponse.product_detail[0])
                setupSpinner(productDetailResponse.product_packing)
                setupList(productDetailResponse.youmay_alsolike)
            } else {
                showSnackBar(productDetailResponse.message, requireActivity())
            }
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun setupViews(productDetailResponse: ProductDetailResponse.ProductDetail) {
        binding.tvSweetName.text = productDetailResponse.name
        if (productDetailResponse.favourite == "yes") {
            productLiked = true
            binding.ivFavIcon.setBackgroundResource(R.drawable.favorite_button_active)
        } else {
            productLiked = false
            binding.ivFavIcon.setBackgroundResource(R.drawable.favorite_button)
        }

        fragmentList.clear()
        fragmentList.add(ProductDescriptionFragment.newInstance(productDetailResponse.description))
        fragmentList.add(ProductDescriptionFragment.newInstance((productDetailResponse.nutrition)))

        //tabFragmentAdapter = TabFragmentAdapter(this, fragmentList, 2)
        //binding.vpProductDescNut.adapter = tabFragmentAdapter

        binding.wvProductDetail.setBackgroundColor(Color.TRANSPARENT)
        binding.wvProductDetail.loadDataWithBaseURL(
            null,
            productDetailResponse.description,
            "text/html",
            "UTF-8",
            null
        )

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Description"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Nutrition Value"))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.wvProductDetail.loadDataWithBaseURL(
                            null,
                            productDetailResponse.description,
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                    else -> {
                        binding.wvProductDetail.loadDataWithBaseURL(
                            null,
                            productDetailResponse.nutrition,
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
//        TabLayoutMediator(binding.tabLayout, binding.vpProductDescNut) { tab, position ->
//            when (position) {
//                0 -> {
//                    tab.text = "Description"
//                }
//                else -> {
//                    tab.text = "Nutrition Value"
//                }
//            }
//        }.attach()
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
            (requireActivity() as HomeActivity).switchFragment(
                newInstance(it.product_id),
                addToBackStack = true, addInsteadOfReplace = true
            )
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
        binding.vpProductDetail.adapter = adapter

        TabLayoutMediator(binding.tlProductDetail, binding.vpProductDetail) { tab, position ->
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

    private fun callAddToFav() {
        if (isConnected(requireContext())) {
            categoryViewModel.addFavProduct(
                AddFavRequest(
                    SPreferenceManager.getInstance(requireContext()).session,
                    pid
                )
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun removeFavProduct() {
        if (isConnected(requireContext())) {
            categoryViewModel.removeFavProduct(
                AddFavRequest(
                    SPreferenceManager.getInstance(requireContext()).session,
                    pid
                )
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun handleFavResponse(addFavResponse: AddFavResponse?) {
        binding.pbFav.gone()
        binding.ivFavIcon.visible()
        if (null != addFavResponse) {
            if (addFavResponse.status == 1) {
                productLiked = true
                binding.ivFavIcon.setBackgroundResource(R.drawable.favorite_button_active)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun handleRemoveFavResponse(addFavResponse: AddFavResponse?) {
        binding.pbFav.gone()
        binding.ivFavIcon.visible()
        if (null != addFavResponse) {
            if (addFavResponse.status == 1) {
                productLiked = false
                binding.ivFavIcon.setBackgroundResource(R.drawable.favorite_button)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }
}