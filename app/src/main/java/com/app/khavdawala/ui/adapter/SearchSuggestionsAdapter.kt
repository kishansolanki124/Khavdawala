package com.app.khavdawala.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.khavdawala.R
import com.app.khavdawala.apputils.loadImage
import com.app.khavdawala.pojo.response.ProductListResponse

class SearchSuggestionsAdapter(
    private val items: ArrayList<ProductListResponse.Products?>?, private val context: Context,
    private val mListener: (ProductListResponse.Products?) -> Unit
) :
    RecyclerView.Adapter<SearchSuggestionViewHolder>() {

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestionViewHolder {
        return SearchSuggestionViewHolder(
            LayoutInflater.from(context).inflate(R.layout.search_suggestion_item, parent, false)
        )
    }

    override fun onBindViewHolder(holderCategories: SearchSuggestionViewHolder, position: Int) {

        holderCategories.tvSearchSuggestion.text = items!![position]!!.name
        holderCategories.ivProductSearch.loadImage(items[position]!!.up_pro_img)

        holderCategories.llSearchSuggestionRoot.setOnClickListener {
            items[position].let { it1 -> mListener.invoke(it1) }
        }
    }

    fun addAll(itemList: List<ProductListResponse.Products?>?) {
        items!!.clear()
        items.addAll(itemList!!)
        notifyDataSetChanged()
    }

    fun clear() {
        items?.let {
            val earlierSize = it.size
            it.clear()
            notifyItemRangeRemoved(0, earlierSize)
        }
    }
}

class SearchSuggestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvSearchSuggestion: AppCompatTextView = view.findViewById(R.id.tvSearchSuggestion)
    val ivProductSearch: ImageView = view.findViewById(R.id.iv_product_search)
    val llSearchSuggestionRoot: LinearLayoutCompat = view.findViewById(R.id.llSearchSuggestionRoot)
}