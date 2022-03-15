package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.khavdawala.apputils.AppConstants
import com.app.khavdawala.apputils.rupeesWithTwoDecimal
import com.app.khavdawala.databinding.ActivityOrderSuccessBinding

class OrderSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderSuccessBinding
    private var orderId = ""
    private var paymentId = ""
    private var totalPaidAmount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.rlCart.visibility = View.GONE
        binding.toolbar.ibBack.visibility = View.VISIBLE
        binding.toolbar.ibBack.setOnClickListener {
            //finish all previous activities
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        orderId = intent.getStringExtra(AppConstants.orderId)!!
        paymentId = intent.getStringExtra(AppConstants.paymentId)!!
        totalPaidAmount = intent.getStringExtra(AppConstants.totalPaidAmount)!!

        binding.tvOrderNo.text = orderId
        binding.tvPaidAmount.text = rupeesWithTwoDecimal(totalPaidAmount.toDouble())
        binding.tvPaymentId.text = paymentId
    }

    override fun onBackPressed() {
        //finish all previous activities
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}