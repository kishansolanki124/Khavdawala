package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.FragmentCategoryProductListBinding
import com.app.khavdawala.pojo.request.AddFavRequest
import com.app.khavdawala.pojo.request.FavProductRequest
import com.app.khavdawala.pojo.response.AddFavResponse
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.activity.HomeActivity
import com.app.khavdawala.ui.adapter.FavProductListAdapter
import com.app.khavdawala.viewmodel.ProductViewModel

class FavoriteListFragment : Fragment() {

    private var loading = false
    private var totalRecords = 0
    private var pageNo = 0

    private lateinit var categoryProductListAdapter: FavProductListAdapter
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

        binding.ivCategoryHeader.setBackgroundResource(R.drawable.banner_top6)
        binding.tvHeader.text = getString(R.string.Favourite)
        initRecyclerView()

        categoryViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        categoryViewModel.categoryResponse().observe(requireActivity()) {
            handleResponse(it)
        }

        categoryViewModel.removeFavResponse().observe(requireActivity()) {
            handleRemoveFavResponse(it)
        }
        getProducts()
    }

    private fun handleRemoveFavResponse(addFavResponse: AddFavResponse?) {
        if (null != addFavResponse) {
            if (addFavResponse.status == 1) {
                val index = productList.indexOfFirst {
                    it.product_id == addFavResponse.product_id
                }
                productList[index].favourite = ""
                productList[index].isLoading = false

                productList.removeAt(index)
                categoryProductListAdapter.notifyItemRemove(index)
            }
        }
    }

    private fun initRecyclerView() {
        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProduct.layoutManager = layoutManager

        categoryProductListAdapter = FavProductListAdapter(itemClick = {
            (requireActivity() as HomeActivity).switchFragment(
                ProductDetailFragment.newInstance(it.product_id),
                addToBackStack = true, addInsteadOfReplace = true
            )
        }, itemFavClick = { customClass, _ ->
            removeFavProduct(customClass)
        }, itemCartClick = { product, position ->
            if (product.available_in_cart) {
                (requireActivity() as HomeActivity).removeFromCart(product)
                productList[position].available_in_cart = false
                categoryProductListAdapter.itemRemovedFromCart(position)
            } else {
                (requireActivity() as HomeActivity).addToCart(product)
                productList[position].available_in_cart = true
                categoryProductListAdapter.itemAddedInCart(position)
            }
        }, dropdownClick = { product, position ->
            productList[position].selectedItemPosition = product.selectedItemPosition
            productList[position].cartPackingId = product.cartPackingId
            categoryProductListAdapter.notifyItemChanged(position)
        }, updateCartClick = { product, position ->
            (requireActivity() as HomeActivity).updateToCart(product)
            //productList[position].available_in_cart = true
            categoryProductListAdapter.notifyItemChanged(position)
        })

        binding.rvProduct.adapter = categoryProductListAdapter

        //disabling blinking effect of recyclerview
        (binding.rvProduct.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        binding.nsvProduct.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    if (!loading && totalRecords != 0 && totalRecords > categoryProductListAdapter.itemCount) {
                        //code to fetch more data for endless scrolling
                        pageNo += 10
                        loading = true
                        binding.bottomLoading.pbCommon.visible()
                        categoryViewModel.getFavProductList(
                            FavProductRequest(
                                pageNo, 10,
                                SPreferenceManager.getInstance(requireContext()).session
                            )
                        )
                    }
                }
            }
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
        loading = false
        binding.bottomLoading.pbCommon.gone()
        if (null != productListResponse && productListResponse.status == "1") {
            if (productListResponse.products_list.isNotEmpty()) {
                totalRecords = productListResponse.total_records
                productList.addAll(productListResponse.products_list)
                if (pageNo == 0) {
                    categoryProductListAdapter.reset()
                    categoryProductListAdapter.addItems(productList)
                } else {
                    categoryProductListAdapter.addItems(productListResponse.products_list)
                }
            } else {
                showSnackBar(productListResponse.message, requireActivity())
            }
        } else if (null != productListResponse) {
            showSnackBar(productListResponse.message, requireActivity())
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
        binding.rvProduct.visibility = View.VISIBLE
        binding.loading.pbCommon.visibility = View.GONE
    }

    private fun getProducts() {
        if (isConnected(requireContext())) {
            binding.rvProduct.visibility = View.GONE
            binding.loading.pbCommon.visibility = View.VISIBLE
            loading = true
            categoryViewModel.getFavProductList(
                FavProductRequest(
                    pageNo, 10,
                    SPreferenceManager.getInstance(requireContext()).session
                )
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }
}