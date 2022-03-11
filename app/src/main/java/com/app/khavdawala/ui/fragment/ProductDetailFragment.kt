package com.app.khavdawala.ui.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.app.khavdawala.pojo.response.ProductListResponse
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
    private var products = ProductListResponse.Products()
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

        setupListeners()
        setupViewModel()
    }

    private fun setupListeners() {
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

        binding.btAdd.setOnClickListener {
            binding.llPlusMin.visible()
            binding.llBlankItem.invisible()

            binding.tvProductCount.text = "1"
            binding.tvProductCount.text = "1"
            products.itemQuantity = 1
            (requireActivity() as HomeActivity).updateToCart(products)
            binding.ivCart.setBackgroundResource(R.drawable.cart_icon_active)
        }

        binding.tvMinus.setOnClickListener {
            var currentProductCount = binding.tvProductCount.text.toString().toInt()
            if (currentProductCount != 0) {
                currentProductCount -= 1
            }
            binding.tvProductCount.text = currentProductCount.toString()


            products.itemQuantity = currentProductCount

            if (currentProductCount == 0) {
                binding.ivCart.setBackgroundResource(R.drawable.cart_button)
                binding.llBlankItem.visible()
                binding.llPlusMin.invisible()
                if (products.available_in_cart) {
                    (requireActivity() as HomeActivity).removeFromCart(products)
                    products.available_in_cart = false
                } else {
                    (requireActivity() as HomeActivity).addToCart(products)
                    products.available_in_cart = true
                }
            } else {
                (requireActivity() as HomeActivity).updateToCart(products)
            }
        }

        binding.tvPlus.setOnClickListener {
            var currentProductCount = binding.tvProductCount.text.toString().toInt()
            if (currentProductCount != 99) {
                currentProductCount += 1
            }
            binding.tvProductCount.text = currentProductCount.toString()
            products.itemQuantity = currentProductCount
            (requireActivity() as HomeActivity).updateToCart(products)
        }
    }

    private fun setupViewModel() {
        categoryViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        categoryViewModel.addFavResponse().observe(requireActivity()) {
            handleFavResponse(it)
        }

        categoryViewModel.removeFavResponse().observe(requireActivity()) {
            handleRemoveFavResponse(it)
        }

        categoryViewModel.productDetailResponse().observe(requireActivity()) {
            handleResponse(it)
        }

        if (isConnected(requireContext())) {
            binding.loading.pbCommon.visible()
            binding.tvSweetName.gone()
            binding.clButtons.gone()
            binding.tabLayout.gone()
            binding.tvYouMayLike.gone()
            binding.rvProduct.gone()
            categoryViewModel.getProductDetail(
                ProductRequest(
                    pid = pid,
                    user_mobile = SPreferenceManager.getInstance(requireContext()).session
                )
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun handleResponse(productDetailResponse: ProductDetailResponse?) {
        binding.loading.pbCommon.gone()
        binding.tvSweetName.visible()
        binding.clButtons.visible()
        binding.tabLayout.visible()
        binding.tvYouMayLike.visible()
        binding.rvProduct.visible()

        if (null != productDetailResponse) {
            if (productDetailResponse.status == "1") {

                products.name = productDetailResponse.product_detail[0].name
                products.product_id = productDetailResponse.product_detail[0].product_id

                for (productPacking in productDetailResponse.product_packing) {
                    val packing = ProductListResponse.Products.Packing()
                    packing.packing_id = productPacking.packing_id
                    packing.product_id = productPacking.product_id
                    packing.product_price = productPacking.product_price
                    packing.product_weight = productPacking.product_weight
                    packing.weight_type = productPacking.weight_type
                    products.packing_list.add(packing)
                }

                setupHorizontalMainNews(productDetailResponse.product_gallery)
                setupViews(productDetailResponse.product_detail[0])
                setupSpinner(productDetailResponse.product_packing)
                setupList(productDetailResponse.youmay_alsolike)

                //check item exist in cart
                checkItemExistInCart()
            } else {
                showSnackBar(productDetailResponse.message, requireActivity())
            }
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun checkItemExistInCart() {
        if (requireContext().checkItemExistInCart(
                products.product_id,
                products.cartPackingId,
                products.packing_list
            )
        ) {
            products.available_in_cart = true
            binding.ivCart.setBackgroundResource(R.drawable.cart_icon_active)
        } else {
            products.available_in_cart = false
            binding.ivCart.setBackgroundResource(R.drawable.cart_button)
        }

        binding.spStateGujaratiSamaj.setSelection(products.selectedItemPosition)
        products.cartPackingId =
            products.packing_list[products.selectedItemPosition].packing_id
        products.itemQuantity = binding.tvProductCount.context.getCartItemCount(
            products.product_id,
            products.cartPackingId
        )

        if (products.available_in_cart && products.itemQuantity > 0) {
            binding.llBlankItem.invisible()
            binding.llPlusMin.visible()
            binding.tvProductCount.text = products.itemQuantity.toString()
        } else {
            binding.llBlankItem.visible()
            binding.llPlusMin.invisible()
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
        if (!productDetailResponse.description.isNullOrEmpty()) {
            fragmentList.add(
                WebViewFragment.newInstance(
                    productDetailResponse.description!!,
                    false
                )
            )
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Description"))

        }

        if (!productDetailResponse.nutrition.isNullOrEmpty()) {
            fragmentList.add(ProductDescriptionFragment.newInstance((productDetailResponse.nutrition!!)))
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Nutrition Value"))
        }

        binding.wvProductDetail.setBackgroundColor(Color.TRANSPARENT)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.wvProductDetail.gone()
                        binding.tvHtml.visible()
                        binding.tvHtml.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(
                                productDetailResponse.description,
                                Html.FROM_HTML_MODE_COMPACT
                            )
                        } else {
                            Html.fromHtml(productDetailResponse.description)
                        }
//                        binding.wvProductDetail.loadDataWithBaseURL(
//                            null,
//                            productDetailResponse.description,
//                            "text/html",
//                            "UTF-8",
//                            null
//                        )
                    }
                    else -> {
                        binding.tvHtml.gone()
                        binding.wvProductDetail.visible()
                        binding.wvProductDetail.loadDataWithBaseURL(
                            null,
                            productDetailResponse.nutrition!!,
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
                onTabSelected(tab)
            }
        })

        binding.tabLayout.getTabAt(0)?.select()
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
            object : MySpinnerItemSelectionListener() {
                override fun onUserItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    dropdownPosition: Int,
                    id: Long
                ) {
                    products.selectedItemPosition = dropdownPosition
                    products.cartPackingId =
                        products.packing_list[dropdownPosition].packing_id

                    checkItemExistInCart()
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