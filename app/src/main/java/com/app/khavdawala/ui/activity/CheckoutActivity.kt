package com.app.khavdawala.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.ActivityCheckoutBinding
import com.app.khavdawala.pojo.request.OrderPlaceRequest
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.pojo.response.RegisterResponse
import com.app.khavdawala.pojo.response.ShippingChargeResponse
import com.app.khavdawala.viewmodel.OrderViewModel

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var shippingChargeResponse: ShippingChargeResponse
    private var totalAmount = 0.0
    private var shippingCharge = 0.0
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        orderViewModel.registerResponse().observe(this) {
            handleResponse(it)
        }

        orderViewModel.shippingChargeResponse().observe(this) {
            handleShippingChargeResponse(it)
        }

        totalAmount = intent.getDoubleExtra(AppConstants.AMOUNT, 0.0)

        binding.tvTotalAmount.text = getString(R.string.total_rs, totalAmount.toString())
        binding.toolbar.ibBack.visibility = View.VISIBLE
        binding.toolbar.rlCart.visibility = View.GONE
        binding.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.etMobile.setText(SPreferenceManager.getInstance(this).session.toString())
        binding.etName.setText(
            SPreferenceManager.getInstance(this).getString(AppConstants.NAME, "").toString()
        )

        binding.btPlaceOrder.setOnClickListener {

            if (areFieldsValid()) {
                if (isConnected(this)) {
                    var productId = ""
                    var productName = ""
                    var packingId = ""
                    var packingWeight = ""
                    var packingWeightType = ""
                    var packingQuantity = ""
                    var packingPrice = ""

                    if (!getCartProductList().isNullOrEmpty()) {
                        for (item in getCartProductList()) {
                            productId = item.product_id + ","
                            productName = item.name + ","
                            packingId = item.cartPackingId + ","
                            packingWeight =
                                item.packing_list[item.selectedItemPosition].product_weight + ","
                            packingWeightType =
                                item.packing_list[item.selectedItemPosition].weight_type + ","
                            packingQuantity = item.itemQuantity.toString() + ","
                            packingPrice =
                                item.packing_list[item.selectedItemPosition].product_price + ","
                        }
                    }

                    orderViewModel.addOrder(
                        OrderPlaceRequest(
                            binding.etName.text.toString(),
                            binding.etEmail.text.toString(),
                            "Kutch",
                            "Bhuj",
                            binding.etDeliveryAddress.text.toString(),
                            "370001",
                            binding.etCity.text.toString(),
                            "Gujarat",
                            "8656265656",
                            binding.etMobile.text.toString(),
                            productId,
                            productName,
                            packingId,
                            packingWeight,
                            packingWeightType,
                            packingQuantity,
                            packingPrice,
                            totalAmount.toString(),
                            "0",
                            "android"
                        )
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet), this)
                }
            }
        }

        if (isConnected(this)) {
            binding.rlCheckout.gone()
            binding.loading.pbCommon.visible()
            orderViewModel.getShippingCharge()
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }

        binding.tvDeliveryCharge.text = getString(R.string.total_rs, "0")
        binding.llCheckoutDetails.gone()
    }

    private fun handleShippingChargeResponse(shippingChargeResponse: ShippingChargeResponse?) {
        binding.loading.pbCommon.gone()
        binding.rlCheckout.visible()
        if (null != shippingChargeResponse) {
            this.shippingChargeResponse = shippingChargeResponse
            setupRadioButtons()
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun setupRadioButtons() {
        binding.rgCity.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.rb_rajkot -> {
                        shippingCharge =
                            shippingChargeResponse.shipping_charge[0].rajkot_shipping.toDouble()

                    }

                    R.id.rb_outside_rajkot -> {
                        shippingCharge =
                            shippingChargeResponse.shipping_charge[0].gujarat_shipping.toDouble()

                    }
                }
            }
        }

        binding.rgState.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.rb_gujarat -> {
                        binding.rgCity.visible()
                        binding.tvCity.visible()
                        binding.rbRajkot.isChecked = true
                        shippingCharge =
                            shippingChargeResponse.shipping_charge[0].rajkot_shipping.toDouble()
                    }

                    R.id.rb_outside_gujarat -> {
                        binding.rgCity.gone()
                        binding.tvCity.gone()
                        shippingCharge =
                            shippingChargeResponse.shipping_charge[0].outof_gujarat_shipping.toDouble()

                    }
                }
            }
        }

        if (shippingChargeResponse.shipping_charge[0].gujarat_active == "yes") {
            binding.rbGujarat.visible()
            binding.rbGujarat.isChecked = true
        } else {
            binding.rbGujarat.gone()
        }

        if (shippingChargeResponse.shipping_charge[0].outofgujarat_active == "yes") {
            binding.rbOutsideGujarat.visible()
        } else {
            binding.rbOutsideGujarat.gone()
        }
    }

    private fun handleResponse(registerResponse: RegisterResponse?) {
        if (null != registerResponse) {
            println(registerResponse)
            if (registerResponse.status.toInt() == 1) {
                showAlertToClearCart(registerResponse.message)
            } else {
                showSnackBar(registerResponse.message, this)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun areFieldsValid(): Boolean {
        if (TextUtils.isEmpty(binding.etMobile.text.toString())) {
            showSnackBar(getString(R.string.mobile_no_empty), this)
            return false
        }

        if (binding.etMobile.text.toString().length != 10) {
            showSnackBar(getString(R.string.invalid_mobile_no), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etCity.text.toString())) {
            showSnackBar(getString(R.string.city_empty), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etName.text.toString())) {
            showSnackBar(getString(R.string.name_empty), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmail.text.toString()
            ).matches()
        ) {
            showSnackBar(getString(R.string.invalid_email), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etDeliveryAddress.text.toString())) {
            showSnackBar(getString(R.string.invalid_address), this)
            return false
        }

        if (!binding.cbIAgree.isChecked) {
            showSnackBar(getString(R.string.kindly_accept_tnc), this)
            return false
        }

        return true
    }

    private fun showAlertToClearCart(message: String?) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog.dismiss()
            clearCart()
            finish()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.btnBG))
    }

    private fun clearCart() {
        var productList: ArrayList<ProductListResponse.Products>? =
            SPreferenceManager.getInstance(this)
                .getList("product", ProductListResponse.Products::class.java)

        if (productList.isNullOrEmpty()) {
            productList = ArrayList()
            SPreferenceManager.getInstance(this).putList("product", productList)
        } else {
            productList.clear()
            SPreferenceManager.getInstance(this).putList("product", productList)
        }
    }
}