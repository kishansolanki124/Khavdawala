package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.apputils.EndlessRecyclerOnScrollListener
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.ActivitySearchBinding
import com.app.khavdawala.pojo.request.ProductRequest
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.adapter.SearchSuggestionsAdapter
import com.app.khavdawala.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private var cid: Int = 0
    private var totalRecords = 0
    private var pageNo = 0
    private var loading = false

    var handler: Handler = Handler()
    var delay: Long = 1000 // 1 seconds after user stops typing
    var lastTextEdit: Long = 0
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchSuggestionsAdapter: SearchSuggestionsAdapter
    private var searchSuggestions: ArrayList<ProductListResponse.Products?>? = ArrayList()
    private lateinit var searchViewModel: SearchViewModel

    private val inputFinishChecker = Runnable {
        if (System.currentTimeMillis() > lastTextEdit + delay - 500) {
            //Typing stopped
            if (binding.pbSearch.isVisible) {
                binding.pbSearch.visibility = View.GONE
                if (binding.layoutToolbar.etSearch.text.toString().trim().length > 2) {
                    searchProducts()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (null != intent &&
            null != intent.getStringExtra(AppConstants.SEARCH_STRING) &&
            intent.getStringExtra(AppConstants.SEARCH_STRING)!!.isNotEmpty()
        ) {
            binding.layoutToolbar.etSearch.setText(intent.getStringExtra(AppConstants.SEARCH_STRING))
            binding.layoutToolbar.ibClear.visibility = View.VISIBLE
        }

        initViewModel()

        setupCategoryRecyclerView()

        binding.layoutToolbar.ibClear.setOnClickListener {
            binding.layoutToolbar.etSearch.setText("")
            binding.layoutToolbar.ibClear.visibility = View.GONE
            binding.rvSearchSuggestions.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
        }

        binding.layoutToolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.layoutToolbar.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.layoutToolbar.etSearch.setRawInputType(InputType.TYPE_CLASS_TEXT)

        binding.layoutToolbar.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //avoid triggering event when text is empty
                if (s.toString().trim().isNotEmpty()) {
                    if (!binding.pbSearch.isVisible) {
                        binding.pbSearch.visibility = View.VISIBLE
                        binding.tvNoDataFound.visibility = View.GONE
                        binding.rvSearchSuggestions.visibility = View.GONE
                        binding.layoutToolbar.ibClear.visibility = View.VISIBLE
                    }
                    //typing initiated
                    println("Typing initiated")
                    lastTextEdit = System.currentTimeMillis()
                    handler.postDelayed(inputFinishChecker, delay)
                } else {
                    if (binding.pbSearch.isVisible) {
                        binding.pbSearch.visibility = View.GONE
                        binding.layoutToolbar.ibClear.visibility = View.GONE
                        binding.rvSearchSuggestions.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //You need to remove this to run only once
                handler.removeCallbacks(inputFinishChecker)
            }
        }
        )
    }

    private fun setupCategoryRecyclerView() {

        val layoutManager = setRecyclerViewLayoutManager(
            binding.rvSearchSuggestions,
            this
        )
        searchSuggestionsAdapter = SearchSuggestionsAdapter(
            searchSuggestions,
            this
        ) {
            //passing the searched result back to the home activity
            val intent = Intent()
            intent.putExtra(AppConstants.SEARCH_STRING, it!!.product_id)
            setResult(AppConstants.RequestCode.SEARCH_ACTIVITY, intent)
            finish()
        }

        binding.rvSearchSuggestions.adapter = searchSuggestionsAdapter

        binding.rvSearchSuggestions.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager, 3) {
            override fun onLoadMore() {
                if (!loading && totalRecords != searchSuggestionsAdapter.itemCount) {
                    loading = true
                    pageNo += 10
                    searchViewModel.getProductList(
                        ProductRequest(
                            cid, pageNo, 10,
                            SPreferenceManager.getInstance(this@SearchActivity).session
                        )
                    )
                }
            }
        })
    }

    private fun initViewModel() {
        searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        searchViewModel.productListResponse()
            .observe(this) { catWiseProductListModel ->
                handleResponse(catWiseProductListModel)
            }
    }


    private fun searchProducts() {
        if (isConnected(this)) {
            binding.tvNoDataFound.visibility = View.GONE
            binding.pbSearch.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rvSearchSuggestions.visibility = View.GONE
            searchViewModel.getProductList(
                ProductRequest(
                    0, pageNo, 10,
                    SPreferenceManager.getInstance(this).session,
                    "",
                    binding.layoutToolbar.etSearch.text.toString()
                )
            )
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun handleResponse(productListResponse: ProductListResponse?) {
        binding.pbSearch.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rvSearchSuggestions.visibility = View.VISIBLE

        if (null != productListResponse && productListResponse.status == "1") {
            totalRecords = productListResponse.total_records
            if (productListResponse.products_list.isNotEmpty()) {
                searchSuggestionsAdapter.addAll(productListResponse.products_list)
//                if (pageNo == 0) {
//                    categoryProductListAdapter.reset()
//                    categoryProductListAdapter.addItems(productList)
//                } else {
//                    categoryProductListAdapter.addItems(productListResponse.products_list)
//                }

//                if (productListResponse.banner_list.isNotEmpty()) {
//                    setupHorizontalMainNews(productListResponse.banner_list)
//                }
            } else {
                showSnackBar(productListResponse.message, this)
            }
        } else if (null != productListResponse) {
            showSnackBar(productListResponse.message, this)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
        loading = false

//        if (null != productListResponse) {
//            println(productListResponse)
//            if (productListResponse.status == 200) {
//
//            } else {
//                searchSuggestionsAdapter.clear()
//                binding.tvNoDataFound.visibility = View.VISIBLE
//                //showSnackBar(catWiseProductListModel.message, this)
//            }
//        } else {
//            searchSuggestionsAdapter.clear()
//            showSnackBar(getString(R.string.something_went_wrong), this)
//        }
    }
}