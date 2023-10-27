package com.app.khavdawala.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.khavdawala.R
import com.app.khavdawala.apputils.AppConstants
import com.app.khavdawala.apputils.TouchImageView2
import com.app.khavdawala.databinding.ActivityDisplayPictureBinding
import com.app.khavdawala.pojo.response.ProductDetailResponse
import com.bumptech.glide.Glide

class DisplayPictureActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private var mAlbumList: ArrayList<ProductDetailResponse.ProductGallery>? = null
    private lateinit var binding: ActivityDisplayPictureBinding
    private var mSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplayPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        mAlbumList =
            intent.getSerializableExtra(AppConstants.IMAGE_LIST) as ArrayList<ProductDetailResponse.ProductGallery>
        val pos = intent.getIntExtra(AppConstants.IMAGE_POSITION, 0)

        binding.viewPager.addOnPageChangeListener(this)
        binding.viewPager.adapter = PictureAdapter(this)
        mSize = if (mAlbumList != null) mAlbumList!!.size else 0
        if (pos < mSize) {
            binding.viewPager.currentItem = pos
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private inner class PictureAdapter(context: Context) :
        PagerAdapter() {
        private val mInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return if (mAlbumList != null) mAlbumList!!.size else 0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = mInflater.inflate(R.layout.item_zoom_image, container, false)

            val tivNewsImage = itemView.findViewById<TouchImageView2>(R.id.tivNewsImage)

//            val circularProgressDrawable = CircularProgressDrawable(context)
//            circularProgressDrawable.strokeWidth = 5f
//            circularProgressDrawable.centerRadius = 30f
//            circularProgressDrawable.start()

            Glide.with(this@DisplayPictureActivity)
                .load(mAlbumList!![position].up_pro_img)
//                .apply(
//                    RequestOptions()
//                        .placeholder(circularProgressDrawable)
//                    //.error(R.drawable.default_image_placeholder)
//                )
                .into(tivNewsImage)

            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, anyType: Any) {
            container.removeView(anyType as ConstraintLayout)
        }
    }
}