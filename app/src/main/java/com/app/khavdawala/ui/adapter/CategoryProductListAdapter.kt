package com.app.khavdawala.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.R
import com.app.khavdawala.databinding.CategoryProductListItemBinding
import com.app.khavdawala.pojo.CustomClass
import com.bumptech.glide.Glide

class CategoryProductListAdapter(
    private val itemClickWeb: (CustomClass) -> Unit
) :
    RecyclerView.Adapter<CategoryProductListAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<CustomClass> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val binding =
            CategoryProductListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(binding, itemClickWeb)
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<CustomClass>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun reset() {
        this.list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: CategoryProductListItemBinding,
        private val itemClickCall: (CustomClass) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

//        constructor(parent: ViewGroup) : this(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_vatan_nu_gham,
//                parent, false
//            )
//        )

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: CustomClass
        ) {
            with(newsPortal) {

                binding.tvMLAName.text = newsPortal.itemName

                Glide.with(binding.ivMLA.context)
                    .load(newsPortal.image)
                    .into(binding.ivMLA)

                setupSpinner(binding.spStateGujaratiSamaj)
//
//                if (newsPortal.name.isNullOrEmpty()) {
//                    binding.tvNewsPortalTitle.visibility = View.GONE
//                }
//                if (newsPortal.address.isNullOrEmpty()) {
//                    binding.llNewsPortalAddress.visibility = View.GONE
//                }
//                if (newsPortal.contact_no.isNullOrEmpty()) {
//                    binding.llNewsPortalPhone.visibility = View.GONE
//                }
//                if (newsPortal.email.isNullOrEmpty()) {
//                    binding.llNewsPortalEmail.visibility = View.GONE
//                }
//                if (newsPortal.website.isNullOrEmpty()) {
//                    binding.llNewsPortalWebsite.visibility = View.GONE
//                }
//
//                binding.tvNewsPortalTitle.text = newsPortal.name
//                binding.tvNewsPortalAddress.text = newsPortal.address
//                binding.tvNewsPortalPhone.text = newsPortal.contact_no
//                binding.tvNewsPortalEmail.text = newsPortal.email
//                binding.tvNewsPortalWebsite.text = newsPortal.website
//
                binding.root.setOnClickListener {
                    itemClickCall(this)
                }

                binding.tvMinus.setOnClickListener {
                    var currentProductCount = binding.tvProductCount.text.toString().toInt()
                    if (currentProductCount != 0) {
                        currentProductCount -= 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                }

                binding.tvPlus.setOnClickListener {
                    var currentProductCount = binding.tvProductCount.text.toString().toInt()
                    if (currentProductCount != 10) {
                        currentProductCount += 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                }
//
//                binding.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }

        private fun setupSpinner(spStateGujaratiSamaj: AppCompatSpinner) {
            val stateList: ArrayList<String> = ArrayList()
            //stateList.add(GujratiSamajResponse.State("", getString(R.string.select_state)))
            //stateList.addAll(gujratiSamajResponse.state_list)

            stateList.add("Rs. 50 (250 Gram)")
            stateList.add("Rs. 100 (500 Gram)")
            val adapter: ArrayAdapter<String> = ArrayAdapter(
                spStateGujaratiSamaj.context,
                R.layout.simple_spinner_dropdown_item,
                stateList
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spStateGujaratiSamaj.adapter = adapter

            spStateGujaratiSamaj.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                    stateId = stateList[p2].id!!
//                    filterCitySpinnerList(stateId)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        return
                    }
                }
            //resetSpinners = false
        }
    }
}
//todo work here, state not maintained