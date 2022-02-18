package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.khavdawala.CustomClass
import com.app.khavdawala.R
import com.app.khavdawala.databinding.FragmentHomeBinding
import com.app.khavdawala.ui.adapter.DharasabhyoAdapter

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
    }
}