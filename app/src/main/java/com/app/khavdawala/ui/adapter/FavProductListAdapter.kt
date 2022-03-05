package com.app.khavdawala.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.R
import com.app.khavdawala.apputils.gone
import com.app.khavdawala.apputils.invisible
import com.app.khavdawala.apputils.visible
import com.app.khavdawala.databinding.CategoryProductListItemBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.bumptech.glide.Glide

class FavProductListAdapter(
    private val itemClickWeb: (ProductListResponse.Products) -> Unit,
    private val itemFavClick: (ProductListResponse.Products, Int) -> Unit
) :
    RecyclerView.Adapter<FavProductListAdapter.HomeOffersViewHolder>() {

    var list: ArrayList<ProductListResponse.Products> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val binding =
            CategoryProductListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(binding, itemClickWeb, itemFavClick)
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        println("onBindViewHolder is called $position")
        holder.bindForecast(list[position], position)
    }

    fun addItems(list: ArrayList<ProductListResponse.Products>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun notifyItemRemove(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun reset() {
        this.list.clear()
        notifyItemRangeRemoved(0, this.list.size)
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: CategoryProductListItemBinding,
        private val itemClickCall: (ProductListResponse.Products) -> Unit,
        private val itemFavClick: (ProductListResponse.Products, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindForecast(
            newsPortal: ProductListResponse.Products,
            position: Int
        ) {
            with(newsPortal) {

                binding.tvMLAName.text = newsPortal.name

                if (newsPortal.isLoading) {
                    binding.ivFavIcon.invisible()
                    binding.pbFav.visible()
                    println("item is loading: ${newsPortal.isLoading}")
                } else {
                    binding.ivFavIcon.visible()
                    binding.pbFav.gone()
                    println("item is loading: ${newsPortal.isLoading}")
                }

                binding.ivFavIcon.setBackgroundResource(R.drawable.favorite_button_active)

                Glide.with(binding.ivMLA.context)
                    .load(newsPortal.up_pro_img)
                    .into(binding.ivMLA)

                binding.root.setOnClickListener {
                    itemClickCall(this)
                }

                binding.ivFavIcon.setOnClickListener {
                    newsPortal.isLoading = true
                    binding.pbFav.visible()
                    binding.ivFavIcon.invisible()
                    itemFavClick(newsPortal, position)
                }

                //binding.spCatProduct.tag = position

                val stateList: ArrayList<ProductListResponse.Products.Packing> = ArrayList()
                //stateList.add(GujratiSamajResponse.State("", getString(R.string.select_state)))
                //stateList.addAll(gujratiSamajResponse.state_list)

                stateList.addAll(newsPortal.packing_list)
                //stateList.add("Rs. 100 (500 Gram)")
                val adapter: ArrayAdapter<ProductListResponse.Products.Packing> = ArrayAdapter(
                    binding.spCatProduct.context,
                    R.layout.spinner_display_item,
                    stateList
                )

                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

                binding.spCatProduct.adapter = adapter

                binding.spCatProduct.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
//                    stateId = stateList[p2].id!!
//                    filterCitySpinnerList(stateId)

                            newsPortal.selectedItemPosition = p2
//                        if (selectionCount++ > 1) {
//                            //onItemSelected(p2)
//                            //newsPortal.let { it1 -> itemClickWeb.invoke(it1) }
//                        }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            return
                        }
                    }

                binding.spCatProduct.setSelection(newsPortal.selectedItemPosition)

                binding.tvMinus.setOnClickListener {
                    var currentProductCount = binding.tvProductCount.text.toString().toInt()
                    if (currentProductCount != 0) {
                        currentProductCount -= 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                }

                binding.tvPlus.setOnClickListener {
                    var currentProductCount = binding.tvProductCount.text.toString().toInt()
                    if (currentProductCount != 99) {
                        currentProductCount += 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                }
            }
        }
    }
}