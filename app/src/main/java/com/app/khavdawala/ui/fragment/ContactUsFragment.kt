package com.app.khavdawala.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.hideKeyboard
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.databinding.FragmentContactUsBinding
import com.app.khavdawala.pojo.response.ContactUsResponse
import com.app.khavdawala.pojo.response.RegisterResponse
import com.app.khavdawala.viewmodel.StaticPageViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        staticPageViewModel.commonResponse().observe(requireActivity()) {
            handleContactUsResponse(it)
        }

        fetchMagazineList()

        setupViews()
    }

    private fun handleContactUsResponse(contactUsResponse: RegisterResponse?) {
        binding.btSubmitRegister.visibility = View.VISIBLE
        binding.pbRegister.visibility = View.GONE
        if (null != contactUsResponse) {
            binding.etName.setText("")
            binding.etMobile.setText("")
            binding.etEmail.setText("")
            binding.etSuggestion.setText("")
            showAlertDialog(contactUsResponse.message!!)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun setupViews() {
        binding.tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${binding.tvPhone.text}")
            startActivity(intent)
        }

        binding.tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.tvEmail.text.toString()))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us")
            startActivity(Intent.createChooser(intent, "Email via..."))
        }

        binding.btSubmitRegister.setOnClickListener {
            if (areFieldsValid()) {
                if (isConnected(requireContext())) {
                    hideKeyboard(requireActivity())
                    binding.btSubmitRegister.visibility = View.INVISIBLE
                    binding.pbRegister.visibility = View.VISIBLE
                    staticPageViewModel.inquiry(
                        binding.etName.text.toString(),
                        binding.etMobile.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etSuggestion.text.toString()
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet), requireActivity())
                }
            }
        }

        //underline textview so that it looks clickable
        binding.tvEmail.paintFlags = binding.tvEmail.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tvPhone.paintFlags = binding.tvPhone.paintFlags or Paint.UNDERLINE_TEXT_FLAG
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

    private fun areFieldsValid(): Boolean {
        when {
            TextUtils.isEmpty(binding.etName.text.toString()) -> {
                showSnackBar(getString(R.string.name_empty), requireActivity())
                return false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmail.text.toString()
            ).matches() -> {
                showSnackBar(getString(R.string.invalid_email), requireActivity())
                return false
            }

            TextUtils.isEmpty(binding.etMobile.text.toString()) -> {
                showSnackBar(getString(R.string.mobile_no_empty), requireActivity())
                return false
            }
            binding.etMobile.text.toString().length < 10 -> {
                showSnackBar(getString(R.string.invalid_mobile_no), requireActivity())
                return false
            }
            TextUtils.isEmpty(binding.etSuggestion.text.toString()) -> {
                showSnackBar(getString(R.string.invalid_msg_inq), requireActivity())
                return false
            }
            else -> return true
        }
    }

    private fun showAlertDialog(msg: String) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(resources.getString(android.R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
