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
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.loadImage
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.databinding.FragmentCategoryProductListBinding
import com.app.khavdawala.pojo.request.ProductRequest
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.activity.HomeActivity
import com.app.khavdawala.ui.adapter.CategoryProductListAdapter
import com.app.khavdawala.viewmodel.ProductViewModel

class CategoryProductListFragment : Fragment() {

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

        initRecyclerView()

        categoryViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        categoryViewModel.categoryResponse().observe(requireActivity()) {
            handleResponse(it)
        }

        getProducts()
    }

    private fun initRecyclerView() {
        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProduct.layoutManager = layoutManager

        categoryProductListAdapter = CategoryProductListAdapter(itemClickWeb = {
            (requireActivity() as HomeActivity).switchFragment(ProductDetailFragment(), false)
        }, itemFavClick = { customClass, position ->
            productList[position].isFav = !customClass.isFav
            categoryProductListAdapter.updateItem(position)
        })

        binding.rvProduct.adapter = categoryProductListAdapter
    }

    private fun handleResponse(productListResponse: ProductListResponse?) {
        if (null != productListResponse && productListResponse.products_list.isNotEmpty()) {
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
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
        binding.ivCategoryHeader.visibility = View.VISIBLE
        binding.rvProduct.visibility = View.VISIBLE
        binding.pbHome.visibility = View.GONE
    }

    private fun setupHorizontalMainNews(bannerList: java.util.ArrayList<ProductListResponse.Banner>) {
        if (bannerList.isNotEmpty()) {
            binding.ivCategoryHeader.loadImage(bannerList[0].banner_img)
        }
    }

    private fun getProducts() {
        if (isConnected(requireContext())) {
            binding.ivCategoryHeader.visibility = View.GONE
            binding.rvProduct.visibility = View.GONE
            binding.pbHome.visibility = View.VISIBLE
            categoryViewModel.getProductList(ProductRequest(cid, start, end))
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    companion object {
        fun newInstance(cid: Int): CategoryProductListFragment {
            val fragment = CategoryProductListFragment()
            fragment.cid = cid
            return fragment
        }
    }
}