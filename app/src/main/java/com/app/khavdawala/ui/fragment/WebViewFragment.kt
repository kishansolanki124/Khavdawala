package com.app.khavdawala.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.khavdawala.databinding.FragmentWebviewBinding

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebviewBinding
    private var description = ""
    private var keepPadding: Boolean = false

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
//        binding.wvProduct.setBackgroundColor(Color.TRANSPARENT)
//        binding.wvProduct.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null)

        if (!keepPadding) {
            binding.svHtml.setPadding(0, 0, 0, 0)
        }

        binding.tvHtml.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(description)
        }
    }

    companion object {
        fun newInstance(string: String, keepPadding: Boolean): WebViewFragment {
            val fragment = WebViewFragment()
            fragment.description = string
            fragment.keepPadding = keepPadding
            return fragment
        }
    }
}