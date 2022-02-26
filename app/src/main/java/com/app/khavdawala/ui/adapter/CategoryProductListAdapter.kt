package com.app.khavdawala.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.R
import com.app.khavdawala.databinding.CategoryProductListItemBinding
import com.app.khavdawala.pojo.CustomClass
import com.bumptech.glide.Glide

class CategoryProductListAdapter(
    private val itemClickWeb: (CustomClass) -> Unit
) :
    RecyclerView.Adapter<CategoryProductListAdapter.HomeOffersViewHolder>() {

    var list: ArrayList<CustomClass> = ArrayList()

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
        holder.bindForecast(list[position], position, itemClickWeb)
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

        fun bindForecast(
            newsPortal: CustomClass,
            position: Int,
            itemClickWeb: (CustomClass) -> Unit
        ) {
            with(newsPortal) {

                binding.tvMLAName.text = newsPortal.itemName

                Glide.with(binding.ivMLA.context)
                    .load(newsPortal.image)
                    .into(binding.ivMLA)

                binding.root.setOnClickListener {
                    itemClickCall(this)
                }

                //binding.spCatProduct.tag = position

                val stateList: ArrayList<String> = ArrayList()
                //stateList.add(GujratiSamajResponse.State("", getString(R.string.select_state)))
                //stateList.addAll(gujratiSamajResponse.state_list)

                stateList.add("Rs. 50 (250 Gram)")
                stateList.add("Rs. 100 (500 Gram)")
                val adapter: ArrayAdapter<String> = ArrayAdapter(
                    binding.spCatProduct.context,
                    R.layout.simple_spinner_dropdown_item,
                    stateList
                )

                adapter.setDropDownViewResource(R.layout.display_spinner_dropdown_item)

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
                    if (currentProductCount != 10) {
                        currentProductCount += 1
                    }
                    binding.tvProductCount.text = currentProductCount.toString()
                }
            }
        }
    }
}