package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.FragmentCategoryProductListBinding
import com.app.khavdawala.pojo.request.AddFavRequest
import com.app.khavdawala.pojo.request.ProductRequest
import com.app.khavdawala.pojo.response.AddFavResponse
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.activity.HomeActivity
import com.app.khavdawala.ui.adapter.CategoryProductListAdapter
import com.app.khavdawala.viewmodel.ProductViewModel

class CategoryProductListFragment : Fragment() {

    private var categoryTitle = ""
    private var cid: Int = 0
    private var start: Int = 0
    private var end: Int = 10

    private lateinit var categoryProductListAdapter: CategoryProductListAdapter
    private lateinit var categoryViewModel: ProductViewModel
    private var productList: ArrayList<ProductListResponse.Products> = ArrayList()
    private lateinit var binding: FragmentCategoryProductListBinding
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.text = categoryTitle

        initRecyclerView()
        initVIewModel()
        getProducts()
    }

    private fun handleFavResponse(addFavResponse: AddFavResponse?) {
        if (null != addFavResponse) {
            if (addFavResponse.status == 1) {
                val index = productList.indexOfFirst {
                    it.product_id == addFavResponse.product_id
                }
                productList[index].favourite = "yes"
                productList[index].isLoading = false
                categoryProductListAdapter.notifyItemChanged(index)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun handleRemoveFavResponse(addFavResponse: AddFavResponse?) {
        if (null != addFavResponse) {
            if (addFavResponse.status == 1) {
                val index = productList.indexOfFirst {
                    it.product_id == addFavResponse.product_id
                }
                productList[index].favourite = ""
                productList[index].isLoading = false
                categoryProductListAdapter.notifyItemChanged(index)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun initRecyclerView() {
        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProduct.layoutManager = layoutManager

        categoryProductListAdapter = CategoryProductListAdapter(itemClick = {
            (requireActivity() as HomeActivity).switchFragment(
                ProductDetailFragment.newInstance(it.product_id),
                addToBackStack = true, addInsteadOfReplace = true
            )
        }, itemFavClick = { customClass, _ ->
            if (customClass.favourite.isEmpty()) {
                callAddToFav(customClass)
            } else {
                removeFavProduct(customClass)
            }
        })

        binding.rvProduct.adapter = categoryProductListAdapter

        //disabling blinking effect of recyclerview
        (binding.rvProduct.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun initVIewModel() {
        categoryViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        categoryViewModel.categoryResponse().observe(requireActivity()) {
            handleResponse(it)
        }

        categoryViewModel.addFavResponse().observe(requireActivity()) {
            handleFavResponse(it)
        }

        categoryViewModel.removeFavResponse().observe(requireActivity()) {
            handleRemoveFavResponse(it)
        }
    }

    private fun getProducts() {
        if (isConnected(requireContext())) {
            binding.rvProduct.gone()
            binding.loading.pbCommon.visible()
            categoryViewModel.getProductList(
                ProductRequest(
                    cid, start, end,
                    SPreferenceManager.getInstance(requireContext()).session
                )
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun callAddToFav(customClass: ProductListResponse.Products) {
        if (isConnected(requireContext())) {
            categoryViewModel.addFavProduct(
                AddFavRequest(
                    SPreferenceManager.getInstance(requireContext()).session,
                    customClass.product_id
                )
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun removeFavProduct(customClass: ProductListResponse.Products) {
        if (isConnected(requireContext())) {
            categoryViewModel.removeFavProduct(
                AddFavRequest(
                    SPreferenceManager.getInstance(requireContext()).session,
                    customClass.product_id
                )
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun handleResponse(productListResponse: ProductListResponse?) {
        if (null != productListResponse && productListResponse.status == "1") {
            if (productListResponse.products_list.isNotEmpty()) {
                productList.addAll(productListResponse.products_list)
                if (start == 0) {
                    categoryProductListAdapter.reset()
                    categoryProductListAdapter.addItems(productList)
                } else {
                    categoryProductListAdapter.addItems(productListResponse.products_list)
                }

                if (productListResponse.banner_list.isNotEmpty()) {
                    setupHorizontalMainNews(productListResponse.banner_list)
                }
            } else {
                showSnackBar(productListResponse.message, requireActivity())
            }
        } else if (null != productListResponse) {
            showSnackBar(productListResponse.message, requireActivity())
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
        binding.rvProduct.visible()
        binding.loading.pbCommon.gone()
    }

    private fun setupHorizontalMainNews(bannerList: java.util.ArrayList<ProductListResponse.Banner>) {
        if (bannerList.isNotEmpty()) {
            binding.ivCategoryHeader.loadImage(bannerList[0].banner_img)
        }
    }

    companion object {
        fun newInstance(cid: Int, categoryTitle: String): CategoryProductListFragment {
            val fragment = CategoryProductListFragment()
            fragment.cid = cid
            fragment.categoryTitle = categoryTitle
            return fragment
        }
    }
}