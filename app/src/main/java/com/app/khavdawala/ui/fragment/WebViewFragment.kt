package com.app.khavdawala.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.khavdawala.databinding.FragmentWebviewBinding

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebviewBinding
    private var description = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wvProduct.setBackgroundColor(Color.TRANSPARENT)
        binding.wvProduct.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null)
    }

    companion object {
        fun newInstance(string: String): WebViewFragment {
            val fragment = WebViewFragment()
            fragment.description = string
            return fragment
        }
    }
}