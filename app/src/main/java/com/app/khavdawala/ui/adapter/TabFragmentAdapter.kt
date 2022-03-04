package com.app.khavdawala.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabFragmentAdapter(
    fragment: Fragment,
    private val fragmentList: ArrayList<Fragment>,
    val size: Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}