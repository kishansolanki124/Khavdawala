package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.khavdawala.R
import com.app.khavdawala.databinding.ActivityCartBinding
import com.app.khavdawala.pojo.CustomClass
import com.app.khavdawala.ui.adapter.CartProductAdapter

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var govtWorkNewsAdapter: CartProductAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.ivCart.visibility = View.GONE
        binding.toolbar.ibBack.visibility = View.VISIBLE
        binding.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        layoutManager = LinearLayoutManager(this)
        binding.rvMLAs.layoutManager = layoutManager

        govtWorkNewsAdapter = CartProductAdapter {

        }
        binding.rvMLAs.adapter = govtWorkNewsAdapter

        govtWorkNewsAdapter.reset()
        val arrayList: ArrayList<CustomClass> = ArrayList()

        arrayList.add(
            CustomClass(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.product_photo
                )!!, "Sweets"
            )
        )
        arrayList.add(
            CustomClass(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.product_photo
                )!!, "Sweets"
            )
        )
        arrayList.add(
            CustomClass(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.product_photo
                )!!, "Sweets"
            )
        )
        govtWorkNewsAdapter.setItem(arrayList)

        binding.btCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }
}