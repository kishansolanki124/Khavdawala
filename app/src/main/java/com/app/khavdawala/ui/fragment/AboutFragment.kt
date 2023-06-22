package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.FragmentAboutBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.pojo.response.StaticPageResponse
import com.app.khavdawala.ui.adapter.TabFragmentAdapter
import com.app.khavdawala.viewmodel.StaticPageViewModel
import com.google.android.material.tabs.TabLayoutMediator

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private lateinit var staticPageViewModel: StaticPageViewModel
    private lateinit var tabFragmentAdapter: TabFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.text = getString(R.string.About)
        staticPageViewModel = ViewModelProvider(this)[StaticPageViewModel::class.java]

        staticPageViewModel.categoryResponse().observe(requireActivity()) {
            handleResponse(it)
        }

        fetchMagazineList()
    }

    private fun handleResponse(staticPageResponse: StaticPageResponse?) {
        binding.loading.pbCommon.gone()
        if (null != staticPageResponse) {
            binding.vpStaticPage.visible()
            binding.tabLayout.visible()
            setupViewPager(staticPageResponse.staticpage)
            if (staticPageResponse.banner_list.isNotEmpty()) {
                setupHorizontalMainNews(staticPageResponse.banner_list)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun setupViewPager(list: List<StaticPageResponse.Staticpage>) {
        val staticPageList: ArrayList<StaticPageResponse.Staticpage> = ArrayList()
        val fragmentList: ArrayList<Fragment> = ArrayList()
        val expandableDetailList: HashMap<String, String> = HashMap()

        for (item in list) {
            if (item.name.contains("Terms") || item.name.contains("Refund") || item.name.contains("Privacy")
            ) {
                expandableDetailList[item.name] = item.description
            }

            if (item.name.contains("About")) {
                staticPageList.add(item)
                fragmentList.add(WebViewFragment.newInstance(item.description, true))
            }
        }

        staticPageList.add(StaticPageResponse.Staticpage("", "Terms"))
        fragmentList.add(ExpandableWebDataFragment.newInstance(expandableDetailList))

        staticPageList.add(StaticPageResponse.Staticpage("", "Contact Us"))
        fragmentList.add(ContactUsFragment())

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabFragmentAdapter = TabFragmentAdapter(this, fragmentList, staticPageList.size)
        binding.vpStaticPage.adapter = tabFragmentAdapter
        binding.vpStaticPage.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.vpStaticPage) { tab, position ->
            tab.text = staticPageList[position].name
        }.attach()
    }

    private fun fetchMagazineList() {
        if (isConnected(requireContext())) {
            binding.vpStaticPage.gone()
            binding.tabLayout.gone()
            binding.loading.pbCommon.visible()
            staticPageViewModel.getStaticPage()
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun setupHorizontalMainNews(bannerList: java.util.ArrayList<ProductListResponse.Banner>) {
        if (bannerList.isNotEmpty()) {
            binding.ivCategoryHeader.loadImage(bannerList[0].banner_img)
        }
    }

}