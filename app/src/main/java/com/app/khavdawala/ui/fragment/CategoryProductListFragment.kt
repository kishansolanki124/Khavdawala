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
import com.app.khavdawala.R
import com.app.khavdawala.databinding.FragmentCategoryProductListBinding
import com.app.khavdawala.pojo.CustomClass
import com.app.khavdawala.ui.activity.HomeActivity
import com.app.khavdawala.ui.adapter.CategoryProductListAdapter

class CategoryProductListFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: CategoryProductListAdapter
    private var arrayList: ArrayList<CustomClass> = ArrayList()
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

        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMLAs.layoutManager = layoutManager

        govtWorkNewsAdapter = CategoryProductListAdapter(itemClickWeb = {
            (requireActivity() as HomeActivity).switchFragment(ProductDetailFragment(), false)
        }, itemFavClick = { customClass, position ->
            arrayList[position].isFav = !customClass.isFav
            govtWorkNewsAdapter.updateItem(position)
        })

        binding.rvMLAs.adapter = govtWorkNewsAdapter

        govtWorkNewsAdapter.reset()
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
    }
}