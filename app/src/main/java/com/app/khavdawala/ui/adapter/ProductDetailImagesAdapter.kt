package com.app.khavdawala.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.databinding.IntroItemBinding
import com.app.khavdawala.pojo.response.ProductDetailResponse
import com.bumptech.glide.Glide

class ProductDetailImagesAdapter(
    private val itemClick: (ProductDetailResponse.ProductGallery, Int) -> Unit
) : RecyclerView.Adapter<ProductDetailImagesAdapter.HomeOffersViewHolder>() {

    private var list: List<ProductDetailResponse.ProductGallery> = listOf()

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
            list[position].let { it1 -> itemClick.invoke(it1, position) }
        }
        holder.bind(list[position])
    }

    fun setItem(list: List<ProductDetailResponse.ProductGallery>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(private val binding: IntroItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(introImageModel: ProductDetailResponse.ProductGallery) {
            Glide.with(binding.ivHomeViewPager.context)
                .load(introImageModel.up_pro_img)
                .into(binding.ivHomeViewPager)
        }
    }
}