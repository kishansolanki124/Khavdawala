package com.app.khavdawala.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.databinding.NotificationItemBinding
import com.app.khavdawala.pojo.CustomClass

class NotificationListAdapter(
    private val itemClickWeb: (CustomClass) -> Unit
) :
    RecyclerView.Adapter<NotificationListAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<CustomClass> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val binding =
            NotificationItemBinding.inflate(
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
        private val binding: NotificationItemBinding,
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
            }
        }
    }
}