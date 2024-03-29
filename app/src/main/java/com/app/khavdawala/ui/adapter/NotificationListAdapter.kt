package com.app.khavdawala.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.databinding.NotificationItemBinding
import com.app.khavdawala.pojo.response.NotificationResponse

class NotificationListAdapter :
    RecyclerView.Adapter<NotificationListAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<NotificationResponse.Notification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val binding =
            NotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<NotificationResponse.Notification>) {
        val size = this.list.size
        this.list.addAll(list)
        val sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun reset() {
        val totalSize = this.list.size
        this.list.clear()
        notifyItemRangeRemoved(0, totalSize)
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: NotificationItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindForecast(
            newsPortal: NotificationResponse.Notification
        ) {
            binding.tvNotTitle.text = newsPortal.title
            binding.tvNotDesc.text = newsPortal.description
            binding.tvNotTime.text = newsPortal.pdate
        }
    }
}