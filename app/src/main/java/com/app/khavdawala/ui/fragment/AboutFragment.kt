package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.databinding.FragmentAboutBinding
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

        staticPageViewModel = ViewModelProvider(this)[StaticPageViewModel::class.java]

        staticPageViewModel.categoryResponse().observe(requireActivity()) {
            handleResponse(it)
        }

        fetchMagazineList()
    }

    private fun handleResponse(staticPageResponse: StaticPageResponse?) {
        binding.pbHome.visibility = View.GONE
        if (null != staticPageResponse) {
            binding.vpStaticPage.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
            setupViewPager(staticPageResponse.staticpage)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun setupViewPager(list: List<StaticPageResponse.Staticpage>) {
        val staticPage: ArrayList<StaticPageResponse.Staticpage> = ArrayList()
        val fragmentList: ArrayList<Fragment> = ArrayList()
        for (item in list) {
            if (item.name.contains("About") || item.name.contains("Terms")) {
                staticPage.add(item)
                fragmentList.add(WebViewFragment.newInstance(item.description))
            }
        }

        staticPage.add(StaticPageResponse.Staticpage("", "Contact Us"))
        fragmentList.add(ContactUsFragment())

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabFragmentAdapter = TabFragmentAdapter(this, fragmentList, staticPage.size)
        binding.vpStaticPage.adapter = tabFragmentAdapter
        binding.vpStaticPage.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.vpStaticPage) { tab, position ->
            tab.text = staticPage[position].name
        }.attach()
    }

    private fun fetchMagazineList() {
        if (isConnected(requireContext())) {
            binding.vpStaticPage.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
            binding.pbHome.visibility = View.VISIBLE
            staticPageViewModel.getStaticPage()
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }
}