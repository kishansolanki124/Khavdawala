package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.khavdawala.R
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.loadImage
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.databinding.FragmentNotificationBinding
import com.app.khavdawala.pojo.response.NotificationResponse
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.ui.adapter.NotificationListAdapter
import com.app.khavdawala.viewmodel.StaticPageViewModel

class NotificationListFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: NotificationListAdapter
    private lateinit var staticPageViewModel: StaticPageViewModel
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.text = getString(R.string.Notification)
        layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotification.layoutManager = layoutManager

        govtWorkNewsAdapter = NotificationListAdapter()
        binding.rvNotification.adapter = govtWorkNewsAdapter

        staticPageViewModel = ViewModelProvider(this)[StaticPageViewModel::class.java]

        staticPageViewModel.notificationResponse().observe(requireActivity()) {
            handleResponse(it)
        }

        if (isConnected(requireContext())) {
            binding.rvNotification.visibility = View.GONE
            binding.loading.pbCommon.visibility = View.VISIBLE
            staticPageViewModel.getNotification()
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun handleResponse(notificationResponse: NotificationResponse?) {
        binding.loading.pbCommon.visibility = View.GONE
        if (null != notificationResponse) {
            binding.rvNotification.visibility = View.VISIBLE
            setupList(notificationResponse.notification_list)
            if (notificationResponse.banner_list.isNotEmpty()) {
                setupHorizontalMainNews(notificationResponse.banner_list)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun setupList(notificationList: ArrayList<NotificationResponse.Notification>) {
        govtWorkNewsAdapter.reset()
        govtWorkNewsAdapter.setItem(notificationList)
    }

    private fun setupHorizontalMainNews(bannerList: java.util.ArrayList<ProductListResponse.Banner>) {
        if (bannerList.isNotEmpty()) {
            binding.ivCategoryHeader.loadImage(bannerList[0].banner_img)
        }
    }
}