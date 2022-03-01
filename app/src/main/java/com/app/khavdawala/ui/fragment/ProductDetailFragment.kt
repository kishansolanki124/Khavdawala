package com.app.khavdawala.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.R
import com.app.khavdawala.databinding.FragmentProductDetailBinding
import com.app.khavdawala.pojo.CustomClass
import com.app.khavdawala.ui.activity.HomeActivity
import com.app.khavdawala.ui.adapter.DemoCollectionAdapter
import com.app.khavdawala.ui.adapter.HorizontalProductListAdapter
import com.app.khavdawala.ui.adapter.IntroAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ProductDetailFragment : Fragment() {

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var govtWorkNewsAdapter: HorizontalProductListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmmentList: ArrayList<Fragment> = ArrayList()
        fragmmentList.add(ProductDescriptinoFragment())
        fragmmentList.add(ProductDescriptinoFragment())
        demoCollectionAdapter = DemoCollectionAdapter(this, fragmmentList, 2)
        binding.pager.adapter = demoCollectionAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Description"
                }
                else -> {
                    tab.text = "Nutrition Value"
                }

            }

        }.attach()

        val imageList: ArrayList<Drawable> = ArrayList()
        imageList.add(ContextCompat.getDrawable(requireContext(), R.drawable.banner_1)!!)
        imageList.add(ContextCompat.getDrawable(requireContext(), R.drawable.banner_1)!!)
        imageList.add(ContextCompat.getDrawable(requireContext(), R.drawable.banner_1)!!)
        setupHorizontalMainNews(imageList)

        setupList()
        setupSpinner()
    }

    private fun setupSpinner() {
        val stateList: ArrayList<String> = ArrayList()
        //stateList.add(GujratiSamajResponse.State("", getString(R.string.select_state)))
        //stateList.addAll(gujratiSamajResponse.state_list)

        stateList.add("Rs. 50 (250 Gram)")
        stateList.add("Rs. 100 (500 Gram)")
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            binding.spStateGujaratiSamaj.context,
            R.layout.simple_spinner_dropdown_item,
            stateList
        )

        adapter.setDropDownViewResource(R.layout.display_spinner_dropdown_item)

        binding.spStateGujaratiSamaj.adapter = adapter

        binding.spStateGujaratiSamaj.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
//                    stateId = stateList[p2].id!!
//                    filterCitySpinnerList(stateId)

//                        if (selectionCount++ > 1) {
//                            //onItemSelected(p2)
//                            //newsPortal.let { it1 -> itemClickWeb.invoke(it1) }
//                        }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    return
                }
            }
    }

    private fun setupList() {
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvMLAs.layoutManager = layoutManager

        govtWorkNewsAdapter = HorizontalProductListAdapter {
            (requireActivity() as HomeActivity).switchFragment(ProductDetailFragment(), false)
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


        govtWorkNewsAdapter.setItem(arrayList)
    }

    private fun setupHorizontalMainNews(scrollNewsList: List<Drawable>) {
        val adapter = IntroAdapter {
//            startActivity(
//                Intent(
//                    requireActivity(), NewsDetailsActivity::class.java
//                ).putExtra(AppConstants.NEWS_ID, it.id)
//            )
        }
        //todo work here
        //adapter.setItem(scrollNewsList)
        binding.newsHomeViewPager.adapter = adapter

        TabLayoutMediator(binding.introTabLayout, binding.newsHomeViewPager) { tab, position ->
            println("selected tab is $tab and position is $position")
        }.attach()

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