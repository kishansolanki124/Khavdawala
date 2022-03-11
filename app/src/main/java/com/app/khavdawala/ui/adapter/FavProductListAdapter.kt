package com.app.khavdawala.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.CategoryProductListItemBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.bumptech.glide.Glide

class FavProductListAdapter(
    private val itemClick: (ProductListResponse.Products) -> Unit,
    private val itemFavClick: (ProductListResponse.Products, Int) -> Unit,
    private val itemCartClick: (ProductListResponse.Products, Int) -> Unit,
    private val updateCartClick: (ProductListResponse.Products, Int) -> Unit,
    private val dropdownClick: (ProductListResponse.Products, Int) -> Unit
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
        return HomeOffersViewHolder(
            binding,
            itemClick,
            itemFavClick,
            itemCartClick,
            updateCartClick,
            dropdownClick
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position], position)
    }

    fun addItems(newList: ArrayList<ProductListResponse.Products>) {
//        this.list.addAll(list)
//        notifyDataSetChanged()
        val size = this.list.size
        this.list.addAll(newList)
        val sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun notifyItemRemove(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    fun itemAddedInCart(position: Int) {
        list[position].available_in_cart = true
        notifyItemChanged(position)
    }

    fun itemRemovedFromCart(position: Int) {
        list[position].available_in_cart = false
        notifyItemChanged(position)
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
        private val itemCartClick: (ProductListResponse.Products, Int) -> Unit,
        private val updateCartClick: (ProductListResponse.Products, Int) -> Unit,
        private val dropdownClick: (ProductListResponse.Products, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindForecast(
            product: ProductListResponse.Products,
            position: Int
        ) {
            with(product) {

                binding.tvMLAName.text = product.name

                if (product.isLoading) {
                    binding.ivFavIcon.invisible()
                    binding.pbFav.visible()
                } else {
                    binding.ivFavIcon.visible()
                    binding.pbFav.gone()
                }

                binding.ivFavIcon.setBackgroundResource(R.drawable.favorite_button_active)

                if (binding.ivCart.context.checkItemExistInCart(
                        product.product_id,
                        product.cartPackingId,
                        product.packing_list
                    )
                ) {
                    product.available_in_cart = true
                    binding.ivCart.setBackgroundResource(R.drawable.cart_icon_active)
                } else {
                    product.available_in_cart = false
                    binding.ivCart.setBackgroundResource(R.drawable.cart_button)
                }

                Glide.with(binding.ivMLA.context)
                    .load(product.up_pro_img)
                    .into(binding.ivMLA)

                binding.root.setOnClickListener {
                    itemClickCall(this)
                }

                binding.ivFavIcon.setOnClickListener {
                    product.isLoading = true
                    binding.pbFav.visible()
                    binding.ivFavIcon.invisible()
                    itemFavClick(product, position)
                }

                binding.tvMinus.setOnClickListener {
                    var currentProductCount = binding.tvProductCount.text.toString().toInt()
                    if (currentProductCount != 0) {
                        currentProductCount -= 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                    product.itemQuantity = currentProductCount

                    if (currentProductCount == 0) {
                        binding.llBlankItem.visible()
                        binding.llPlusMin.invisible()
                        itemCartClick(product, position)
                    } else {
                        updateCartClick(product, position)
                    }
                }

                binding.btAdd.setOnClickListener {
                    binding.llPlusMin.visible()
                    binding.llBlankItem.invisible()

                    binding.tvProductCount.text = "1"
                    product.itemQuantity = 1

                    updateCartClick(product, position)
                }

                binding.tvPlus.setOnClickListener {
                    var currentProductCount = binding.tvProductCount.text.toString().toInt()
                    if (currentProductCount != 99) {
                        currentProductCount += 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                    product.itemQuantity = currentProductCount

                    updateCartClick(product, position)
                }

                binding.ivCart.setOnClickListener {
                    //itemCartClick(product, position)
                }

                val stateList: ArrayList<ProductListResponse.Products.Packing> = ArrayList()

                stateList.addAll(product.packing_list)
                //stateList.add("Rs. 100 (500 Gram)")
                val adapter: ArrayAdapter<ProductListResponse.Products.Packing> = ArrayAdapter(
                    binding.spCatProduct.context,
                    R.layout.spinner_display_item,
                    stateList
                )

                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

                binding.spCatProduct.adapter = adapter

                binding.spCatProduct.onItemSelectedListener =
                    object : MySpinnerItemSelectionListener() {
                        override fun onUserItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            dropdownPosition: Int,
                            id: Long
                        ) {
                            product.selectedItemPosition = dropdownPosition
                            product.cartPackingId =
                                product.packing_list[dropdownPosition].packing_id
                            dropdownClick(product, position)
                        }
                    }

                binding.spCatProduct.setSelection(product.selectedItemPosition)
                product.cartPackingId =
                    product.packing_list[product.selectedItemPosition].packing_id
                product.itemQuantity = binding.tvProductCount.context.getCartItemCount(
                    product.product_id,
                    product.cartPackingId
                )

                if (product.available_in_cart && product.itemQuantity > 0) {
                    binding.llBlankItem.invisible()
                    binding.llPlusMin.visible()
                    binding.tvProductCount.text = product.itemQuantity.toString()
                } else {
                    binding.llBlankItem.visible()
                    binding.llPlusMin.invisible()
                }
            }
        }
    }
}