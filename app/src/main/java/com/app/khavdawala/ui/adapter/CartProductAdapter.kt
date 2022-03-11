package com.app.khavdawala.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.R
import com.app.khavdawala.databinding.CheckoutItemBinding
import com.app.khavdawala.pojo.response.ProductListResponse

class CartProductAdapter(
    private val itemClick: (ProductListResponse.Products) -> Unit,
    private val removeFromCartClick: (ProductListResponse.Products, Int) -> Unit,
    private val updateCartClick: (ProductListResponse.Products, Int) -> Unit
) :
    RecyclerView.Adapter<CartProductAdapter.HomeOffersViewHolder>() {

    private var productList: ArrayList<ProductListResponse.Products> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val binding =
            CheckoutItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(binding, itemClick, removeFromCartClick, updateCartClick)
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(productList[position], position)
    }

    fun itemRemovedFromCart(position: Int) {
        productList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, productList.size)
    }

    fun setItem(newList: ArrayList<ProductListResponse.Products>) {
        val size = this.productList.size
        this.productList.addAll(newList)
        val sizeNew = this.productList.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun reset() {
        this.productList.clear()
        notifyItemRangeRemoved(0, this.productList.size)
    }

    override fun getItemCount(): Int = productList.size

    class HomeOffersViewHolder(
        private val binding: CheckoutItemBinding,
        private val itemClickCall: (ProductListResponse.Products) -> Unit,
        private val removeFromCartClick: (ProductListResponse.Products, Int) -> Unit,
        private val updateCartClick: (ProductListResponse.Products, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: ProductListResponse.Products,
            position: Int
        ) {
            with(newsPortal) {

                println("updated item name is: ${newsPortal.name} and position is $position")
                binding.tvMLAName.text = newsPortal.name
                binding.tvProductCount.text = newsPortal.itemQuantity.toString()

                val amount =
                    newsPortal.packing_list[newsPortal.selectedItemPosition].product_price.toDouble() * newsPortal.itemQuantity
                binding.tvAmount.text =
                    binding.tvAmount.context.getString(R.string.total_rs, amount.toString())
                binding.tvWeight.text =
                    "(${newsPortal.packing_list[newsPortal.selectedItemPosition].product_weight} " +
                            "${newsPortal.packing_list[newsPortal.selectedItemPosition].weight_type})"

                binding.root.setOnClickListener {
                    itemClickCall(this)
                }

                binding.ivDelete.setOnClickListener {
                    removeFromCartClick(newsPortal, position)
                }

                binding.tvMinus.setOnClickListener {
                    var currentProductCount = binding.tvProductCount.text.toString().toInt()
                    if (currentProductCount != 0) {
                        currentProductCount -= 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                    newsPortal.itemQuantity = currentProductCount

                    if (currentProductCount == 0) {
                        removeFromCartClick(newsPortal, position)
                    } else {
                        val newAmount =
                            newsPortal.packing_list[newsPortal.selectedItemPosition].product_price.toDouble() * newsPortal.itemQuantity
                        binding.tvAmount.text =
                            binding.tvAmount.context.getString(
                                R.string.total_rs,
                                newAmount.toString()
                            )
                        updateCartClick(newsPortal, position)
                    }
                }

                binding.tvPlus.setOnClickListener {
                    var currentProductCount = binding.tvProductCount.text.toString().toInt()
                    if (currentProductCount != 99) {
                        currentProductCount += 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                    newsPortal.itemQuantity = currentProductCount

                    val newAmount =
                        newsPortal.packing_list[newsPortal.selectedItemPosition].product_price.toDouble() * newsPortal.itemQuantity
                    binding.tvAmount.text =
                        binding.tvAmount.context.getString(R.string.total_rs, newAmount.toString())
                    updateCartClick(newsPortal, position)
                }
            }
        }
    }
}