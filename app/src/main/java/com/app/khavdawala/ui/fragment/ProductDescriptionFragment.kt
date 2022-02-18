package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.khavdawala.databinding.FragmentProductDescriptionBinding

class ProductDescriptinoFragment : Fragment() {

    private lateinit var binding: FragmentProductDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }
}