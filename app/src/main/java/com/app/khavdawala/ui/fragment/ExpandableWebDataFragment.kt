package com.app.khavdawala.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.app.khavdawala.databinding.ExpandableWebviewBinding
import com.app.khavdawala.ui.adapter.CustomizedExpandableListAdapter

class ExpandableWebDataFragment : Fragment() {

    private lateinit var binding: ExpandableWebviewBinding
    private var expandableListAdapter: ExpandableListAdapter? = null
    private var expandableTitleList: List<String>? = null
    private var expandableDetailList: HashMap<String, String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExpandableWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expandableDetailList?.let {
            expandableTitleList = ArrayList(expandableDetailList!!.keys)
            expandableListAdapter =
                CustomizedExpandableListAdapter(
                    requireContext(),
                    expandableTitleList,
                    expandableDetailList
                )
            binding.expandableListViewSample.setAdapter(expandableListAdapter)
            binding.expandableListViewSample.setOnChildClickListener { _: ExpandableListView?, _: View?, _: Int, _: Int, _: Long -> false }
        }
    }

    companion object {
        fun newInstance(expandableDetailList: HashMap<String, String>): ExpandableWebDataFragment {
            val fragment = ExpandableWebDataFragment()
            fragment.expandableDetailList = expandableDetailList
            return fragment
        }
    }
}