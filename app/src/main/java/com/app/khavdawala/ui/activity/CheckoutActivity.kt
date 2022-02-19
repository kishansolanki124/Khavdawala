package com.app.khavdawala.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.khavdawala.R
import com.app.khavdawala.databinding.ActivityCartBinding
import com.app.khavdawala.databinding.ActivityCheckoutBinding
import com.app.khavdawala.pojo.CustomClass
import com.app.khavdawala.ui.adapter.CartProductAdapter

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}