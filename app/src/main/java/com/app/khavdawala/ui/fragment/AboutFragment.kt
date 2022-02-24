package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.khavdawala.databinding.FragmentAboutBinding
import com.app.khavdawala.ui.adapter.DemoCollectionAdapter
import com.google.android.material.tabs.TabLayoutMediator

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private lateinit var demoCollectionAdapter: DemoCollectionAdapter

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

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        val fragmmentList: ArrayList<Fragment> = ArrayList()
        fragmmentList.add(ProductDescriptinoFragment())
        fragmmentList.add(ProductDescriptinoFragment())
        fragmmentList.add(ProductDescriptinoFragment())

        demoCollectionAdapter = DemoCollectionAdapter(this, fragmmentList, 3)
        binding.pager.adapter = demoCollectionAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "About Us"
                }
                1 -> {
                    tab.text = "Terms"
                }
                2 -> {
                    tab.text = "Contact"
                }
            }
        }.attach()
    }
}