package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.khavdawala.R

class HomeFragment : Fragment() {

//    private lateinit var timer: Timer
//    private lateinit var timer2: Timer
//    private lateinit var layoutManager: LinearLayoutManager
//    private var pageNo = 0
//    private var loading = false
//    private var newsCategoryId = 0
//    private var visibleThreshold = 3
//    private var totalRecords = 0
//    private lateinit var newsWithAdsAdapter: NewsWithAdsAdapter
//    private var newsWithBannerAdsList = ArrayList<CustomNewsListWithBanner>()
//    private lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    //    private fun setListener() {
////        ivPrev.setOnClickListener {
////            val currPos: Int = breakingNewsViewPager.currentItem
////            if (currPos != 0) {
////                breakingNewsViewPager.currentItem = currPos - 1
////            }
////        }
//
////        ivNext.setOnClickListener {
////            val currPos: Int = breakingNewsViewPager.currentItem
////            if ((currPos + 1) != breakingNewsViewPager.adapter.itemCount) {
////                breakingNewsViewPager.currentItem = currPos + 1
////            }
////        }
//    }
//
//    fun fetchNews(clearList: Boolean, newsRequest: NewsRequest) {
//        if (clearList) {
//            newsWithAdsAdapter.clearList()
//        }
//        loading = true
//        newsCategoryId = newsRequest.cid
//        pageNo = newsRequest.start
//        newsWithBannerAdsList = ArrayList()
//        if (isConnected(requireContext())) {
//            //showProgressDialog(requireContext())
//            newsViewModel.getNews(newsRequest)
//        } else {
//            showSnackBar(getString(R.string.no_internet), requireActivity())
//        }
//    }
//
//    private fun handleResponseOfNewsResponse(newsResponse: NewsResponse?) {
//        dismissProgressDialog()
//        if (null != newsResponse) {
//
//            if (newsResponse.version_code != 0) {
//                val versionCode: Int = BuildConfig.VERSION_CODE
//                if (versionCode < newsResponse.version_code) {
//                    showAlertDialog()
//                }
//            }
//
//
//            if (null == newsResponse.total_records && pageNo == 0) {
//                showSnackBar(getString(R.string.no_news_found), requireActivity())
//                loading = false
//                return
//            }
//
//            if (pageNo == 0) {
//                setupHorizontalMainNews(newsResponse.scroll_news_list)
//                setupBreakingNews(newsResponse.breaking_news_list)
//            }
//
//            totalRecords = newsResponse.total_records!!
//
//            setupNewsList(
//                newsResponse.news_list,
//                newsResponse.news_banner
//            )
//        } else {
//            loading = false
//            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
//        }
//    }
//
//    private fun setupBreakingNews(breakingNewsList: List<NewsResponse.BreakingNews>) {
//
//        var newsString = ""
//        for (item in breakingNewsList) {
//            newsString = newsString.plus(item.name)
//            newsString = newsString.plus("   ||   ")
//        }
//
//        tvScrollableNews.ellipsize = TextUtils.TruncateAt.MARQUEE
//        tvScrollableNews.text = newsString
//        tvScrollableNews.isSelected = true
//        tvScrollableNews.isSingleLine = true
//
////        val adapter = BreakingNewsAdapter {
////            startActivity(
////                Intent(
////                    requireActivity(), NewsDetailsActivity::class.java
////                ).putExtra(AppConstants.NEWS_ID, it.id)
////            )
////        }
////        adapter.setItem(breakingNewsList)
////        breakingNewsViewPager.adapter = adapter
////
////        val handler = Handler(Looper.myLooper()!!)
////        var currentPage = 0
////        val update = Runnable {
////            if (currentPage == breakingNewsList.size) {
////                currentPage = 0
////            }
////
////            if (null != breakingNewsViewPager) {
////                breakingNewsViewPager.setCurrentItem(currentPage++, true)
////            }
////        }
////
////        timer2 = Timer()
////        timer2.schedule(object : TimerTask() {
////            override fun run() {
////                handler.post(update)
////            }
////        }, 4000, 4000)
//    }
//
//    private fun setupHorizontalMainNews(scrollNewsList: List<NewsResponse.ScrollNews>) {
//        val adapter = IntroAdapter {
//            startActivity(
//                Intent(
//                    requireActivity(), NewsDetailsActivity::class.java
//                ).putExtra(AppConstants.NEWS_ID, it.id)
//            )
//        }
//        adapter.setItem(scrollNewsList)
//        newsHomeViewPager.adapter = adapter
//
//        TabLayoutMediator(introTabLayout, newsHomeViewPager) { tab, position ->
//            println("selected tab is $tab and position is $position")
//        }.attach()
//
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
//    }
//
//    private fun setupNewsList(
//        newsList: List<NewsResponse.News>?,
//        newsBanner: List<NewsResponse.NewsBanner>
//    ) {
//        if (null == newsList) {
//            loading = false
//            return
//        }
//
//        for ((index, item) in newsList.withIndex()) {
//            //setting banner ad/image as 5th or 10th item in list
//            if (index == 4 || index == 9) {
//                if (index == 9) {
//                    if (newsBanner.size > 1) {
//                        newsWithBannerAdsList.add(
//                            CustomNewsListWithBanner(
//                                2,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                newsBanner[1].id,
//                                newsBanner[1].name,
//                                newsBanner[1].up_pro_img,
//                                newsBanner[1].url
//                            )
//                        )
//                    } else {
//                        newsWithBannerAdsList.add(
//                            CustomNewsListWithBanner(
//                                2,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                newsBanner[0].id,
//                                newsBanner[0].name,
//                                newsBanner[0].up_pro_img,
//                                newsBanner[0].url
//                            )
//                        )
//                    }
//                } else {
//                    newsWithBannerAdsList.add(
//                        CustomNewsListWithBanner(
//                            2,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            newsBanner[0].id,
//                            newsBanner[0].name,
//                            newsBanner[0].up_pro_img,
//                            newsBanner[0].url
//                        )
//                    )
//                }
//            } else {
//                newsWithBannerAdsList.add(
//                    CustomNewsListWithBanner(
//                        1,
//                        item.category_name,
//                        item.cid,
//                        item.id,
//                        item.likes,
//                        item.name,
//                        item.pdate,
//                        item.up_pro_img,
//                        item.views
//                    )
//                )
//            }
//        }
//
//        newsWithAdsAdapter.addAll(newsWithBannerAdsList)
//        loading = false
//    }
//
//    private fun setEndlessScroll() {
//        layoutManager = LinearLayoutManager(requireContext())
//        rvNews.layoutManager = layoutManager
//
//        //After one row add divider or separator
//        val dividerItemDecoration = DividerItemDecoration(rvNews.context, 1)
//        rvNews.addItemDecoration(dividerItemDecoration)
//
////        rvNews.addOnScrollListener(object :
////            EndlessRecyclerOnScrollListener(layoutManager, 3) {
////            override fun onLoadMore() {
////                if (loading) {
////                    return
////                }
////                loading = true
////                pageNo += 10
////                fetchNews(false, NewsRequest(newsCategoryId, pageNo, pageNo + 10))
////            }
////        })
//
//        nsvNews.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
//            val totalItemCount = layoutManager.itemCount
//            val lastVisibleItem = layoutManager
//                .findLastVisibleItemPosition()
//
//            if (v.getChildAt(v.childCount - 1) != null) {
//                val recyclerView: View = v.getChildAt(v.childCount - 1).findViewById(R.id.rvNews)
//                if (scrollY >= recyclerView.measuredHeight - v.measuredHeight &&
//                    scrollY > oldScrollY
//                    &&
//                    totalRecords != newsWithAdsAdapter.itemCount
//                    &&
//                    !loading
//                    && totalItemCount <= lastVisibleItem + visibleThreshold
//                ) {
//                    pageNo += 10
//                    loading = true
//                    fetchNews(false, NewsRequest(newsCategoryId, pageNo, pageNo + 10))
//                    println("Hello world page new loaded is $pageNo")
//                }
//            }
//        }
//
//        newsWithAdsAdapter = NewsWithAdsAdapter(newsWithBannerAdsList, requireContext()) {
//            if (it.item_type == 1) {
//                startActivity(
//                    Intent(
//                        requireActivity(), NewsDetailsActivity::class.java
//                    ).putExtra(AppConstants.NEWS_ID, it.id)
//                )
//            } else {
//                openBrowser(requireContext(), it.banner_url!!)
//            }
//        }
//        rvNews.adapter = newsWithAdsAdapter
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        if (::timer.isInitialized) {
//            timer.cancel()
//        }
//        if (::timer2.isInitialized) {
//            timer2.cancel()
//        }
//    }
//
//    private fun showAlertDialog() {
//        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
//        alertDialogBuilder.setTitle(getString(R.string.new_version_available))
//        alertDialogBuilder.setMessage(getString(R.string.new_version_available_desc))
//        alertDialogBuilder.setCancelable(false)
//
//        alertDialogBuilder.setPositiveButton(
//            getString(R.string.open_play_store)
//        ) { _, _ ->
//            try {
//                startActivity(
//                    Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
//                    )
//                )
//            } catch (e: ActivityNotFoundException) {
//                startActivity(
//                    Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
//                    )
//                )
//            }
//        }
//
//        alertDialogBuilder.setNegativeButton(
//            getString(R.string.exit_app)
//        ) { dialog, _ ->
//            dialog.dismiss()
//            requireActivity().onBackPressed()
//        }
//
//        val alertDialog: AlertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//            .setTextColor(ContextCompat.getColor(requireContext(), R.color.bottomnavBG))
//        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//            .setTextColor(ContextCompat.getColor(requireContext(), R.color.bottomnavBG))
//    }
}