package com.app.khavdawala.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.khavdawala.pojo.CustomClass
import com.app.khavdawala.R
import com.app.khavdawala.databinding.FragmentHomeBinding
import com.app.khavdawala.ui.adapter.DharasabhyoAdapter
import com.app.khavdawala.ui.adapter.IntroAdapter

class HomeFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: DharasabhyoAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMLAs.layoutManager = layoutManager

        govtWorkNewsAdapter = DharasabhyoAdapter {

        }
        binding.rvMLAs.adapter = govtWorkNewsAdapter

        govtWorkNewsAdapter.reset()
        val arrayList: ArrayList<CustomClass> = ArrayList()
        arrayList.add(
            CustomClass(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.product_photo
                )!!, "Sweets"
            )
        )
        arrayList.add(
            CustomClass(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.product_photo
                )!!, "Chevda"
            )
        )
        arrayList.add(
            CustomClass(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.product_photo
                )!!, "Wafers"
            )
        )
        arrayList.add(
            CustomClass(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.product_photo
                )!!, "Handmade Khakhra"
            )
        )
        govtWorkNewsAdapter.setItem(arrayList)

        val imageList: ArrayList<Drawable> = ArrayList()
        imageList.add(ContextCompat.getDrawable(requireContext(), R.drawable.banner_1)!!)
        imageList.add(ContextCompat.getDrawable(requireContext(), R.drawable.banner_1)!!)
        imageList.add(ContextCompat.getDrawable(requireContext(), R.drawable.banner_1)!!)
        setupHorizontalMainNews(imageList)

        binding.ivNext.setOnClickListener {
            binding.newsHomeViewPager.currentItem = binding.newsHomeViewPager.currentItem + 1
        }

        binding.ivPrev.setOnClickListener {
            binding.newsHomeViewPager.currentItem = binding.newsHomeViewPager.currentItem - 1
        }
    }

    private fun setupHorizontalMainNews(scrollNewsList: List<Drawable>) {
        val adapter = IntroAdapter {
//            startActivity(
//                Intent(
//                    requireActivity(), NewsDetailsActivity::class.java
//                ).putExtra(AppConstants.NEWS_ID, it.id)
//            )
        }
        adapter.setItem(scrollNewsList)
        binding.newsHomeViewPager.adapter = adapter

//        TabLayoutMediator(binding.introTabLayout, newsHomeViewPager) { tab, position ->
//            println("selected tab is $tab and position is $position")
//        }.attach()

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
}