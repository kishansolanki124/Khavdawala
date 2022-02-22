package com.app.khavdawala.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.pojo.CustomClass
import com.app.khavdawala.databinding.CategoryItemBinding
import com.bumptech.glide.Glide

class ProductCategoryAdapter(
    private val itemClickWeb: (CustomClass) -> Unit
) :
    RecyclerView.Adapter<ProductCategoryAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<CustomClass> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.dharasabhyo_item, parent, false)
//        return HomeOffersViewHolder(
//            view, itemClickCall, itemClickWeb
//        )

        val binding =
            CategoryItemBinding.inflate(
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
        private val binding: CategoryItemBinding,
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
//
//                binding.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}