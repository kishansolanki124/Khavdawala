package com.app.khavdawala.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.databinding.IntroItemBinding
import com.app.khavdawala.pojo.response.CategoryResponse
import com.bumptech.glide.Glide

class IntroAdapter(
    private val itemClick: (CategoryResponse.Slider) -> Unit
) : RecyclerView.Adapter<IntroAdapter.HomeOffersViewHolder>() {

    private var list: List<CategoryResponse.Slider> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeOffersViewHolder {
        val binding =
            IntroItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            list[position].let { it1 -> itemClick.invoke(it1) }
        }
        holder.bind(list[position])
    }

    fun setItem(list: List<CategoryResponse.Slider>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(private val binding: IntroItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(introImageModel: CategoryResponse.Slider) {
            Glide.with(binding.ivHomeViewPager.context)
                .load(introImageModel.up_pro_img)
                .into(binding.ivHomeViewPager)
        }
    }
}