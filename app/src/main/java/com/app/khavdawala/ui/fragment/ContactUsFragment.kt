package com.app.khavdawala.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.databinding.FragmentContactUsBinding
import com.app.khavdawala.pojo.response.ContactUsResponse
import com.app.khavdawala.viewmodel.StaticPageViewModel

class ContactUsFragment : Fragment() {

    private lateinit var binding: FragmentContactUsBinding
    private lateinit var staticPageViewModel: StaticPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        staticPageViewModel = ViewModelProvider(this)[StaticPageViewModel::class.java]

        staticPageViewModel.contactUs().observe(requireActivity()) {
            handleResponse(it)
        }

        fetchMagazineList()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun handleResponse(staticPageResponse: ContactUsResponse?) {
        binding.pbContactUs.visibility = View.GONE
        binding.llContactUs.visibility = View.VISIBLE
        if (null != staticPageResponse) {
            binding.wbMap.settings.javaScriptEnabled = true
            binding.wbMap.loadData(
                staticPageResponse.contact_us[0].google_map,
                "text/html",
                "utf-8"
            )

            binding.tvName.text = staticPageResponse.contact_us[0].name
            binding.tvAddress.text = staticPageResponse.contact_us[0].address
            binding.tvPhone.text = staticPageResponse.contact_us[0].phone
            binding.tvEmail.text = staticPageResponse.contact_us[0].email
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }

    }

    private fun fetchMagazineList() {
        if (isConnected(requireContext())) {
            binding.llContactUs.visibility = View.GONE
            binding.pbContactUs.visibility = View.VISIBLE
            staticPageViewModel.getContactUs()
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }
}