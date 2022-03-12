package com.app.khavdawala.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.ActivityCheckoutBinding
import com.app.khavdawala.pojo.request.OrderPlaceRequest
import com.app.khavdawala.pojo.response.AddOrderResponse
import com.app.khavdawala.pojo.response.OrderAddressResponse
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.pojo.response.ShippingChargeResponse
import com.app.khavdawala.viewmodel.OrderViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import kotlin.math.roundToInt

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var shippingChargeResponse: ShippingChargeResponse
    private var totalAmount = 0.0
    private var shippingCharge = 0.0
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialising RazorPay SDK
        Checkout.preload(this)

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        orderViewModel.registerResponse().observe(this) {
            handleResponse(it)
        }

        orderViewModel.shippingChargeResponse().observe(this) {
            handleShippingChargeResponse(it)
        }

        orderViewModel.addressResponse().observe(this) {
            handleAddressResponse(it)
        }

        totalAmount = intent.getDoubleExtra(AppConstants.AMOUNT, 0.0)

        binding.tvTotalAmount.text = getString(R.string.total_rs, totalAmount.toString())
        binding.tvGrandTotalAmount.text =
            getString(R.string.total_rs, (shippingCharge + totalAmount).toString())
        binding.toolbar.ibBack.visibility = View.VISIBLE
        binding.toolbar.rlCart.visibility = View.GONE
        binding.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.btMobAddress.setOnClickListener {
            hideKeyboard(this)
            if (binding.etMobile.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.mobile_no_empty), this)
            } else if (binding.etMobile.text.toString().length != 10) {
                showSnackBar(getString(R.string.invalid_mobile_no), this)
            } else {
                if (isConnected(this)) {
                    binding.loadingAddress.pbCommon.visible()
                    binding.llCheckoutDetails.gone()
                    orderViewModel.getAddress(binding.etMobile.text.toString())
                } else {
                    showSnackBar(getString(R.string.no_internet), this)
                }
            }
        }

        binding.btPlaceOrder.setOnClickListener {
            hideKeyboard(this)
            if (areFieldsValid()) {
                if (isConnected(this)) {
                    binding.btPlaceOrder.invisible()
                    binding.pbPlaceOrder.visible()
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
                            binding.etArea.text.toString(),
                            binding.etSubArea.text.toString(),
                            binding.etDeliveryAddress.text.toString(),
                            binding.etZip.text.toString(),
                            binding.etCity.text.toString(),
                            binding.etState.text.toString(),
                            binding.etAlternateMob.text.toString(),
                            binding.etMobile.text.toString(),
                            productId,
                            productName,
                            packingId,
                            packingWeight,
                            packingWeightType,
                            packingQuantity,
                            packingPrice,
                            if (binding.cbGiftPack.isChecked) {
                                "yes"
                            } else "",
                            binding.etNotes.text.toString(),
                            totalAmount.toString(),
                            shippingCharge.toString(),
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

        binding.tvDeliveryChargeAmount.text = getString(R.string.total_rs, "0")
        binding.llCheckoutDetails.gone()
    }

    private fun handleAddressResponse(addressResponse: OrderAddressResponse?) {
        binding.loadingAddress.pbCommon.gone()
        binding.llCheckoutDetails.visible()
        if (null != addressResponse) {
            if (addressResponse.status == "1") {
                setAddressField(addressResponse)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun setAddressField(addressResponse: OrderAddressResponse) {
        binding.etName.setText(addressResponse.address_detail[0].customer_name)
        binding.etEmail.setText(addressResponse.address_detail[0].customer_email)
        binding.etArea.setText(addressResponse.address_detail[0].area)
        binding.etSubArea.setText(addressResponse.address_detail[0].sub_area)
        binding.etZip.setText(addressResponse.address_detail[0].zipcode)
        binding.etCity.setText(addressResponse.address_detail[0].city)
        binding.etDeliveryAddress.setText(addressResponse.address_detail[0].address)
        binding.etMobile.setText(addressResponse.address_detail[0].mobile_no)
        binding.etAlternateMob.setText(addressResponse.address_detail[0].alternate_contact_no)
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
                        binding.etCity.setText(getString(R.string.Rajkot))
                        binding.etState.setText(getString(R.string.Gujarat))
                        calculateDeliveryCharge()
                    }

                    R.id.rb_outside_rajkot -> {
                        binding.etCity.setText("")
                        binding.etState.setText(getString(R.string.Gujarat))
                        calculateDeliveryCharge()
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
                        binding.etCity.setText(getString(R.string.Rajkot))
                        binding.etState.setText(getString(R.string.Gujarat))
                        calculateDeliveryCharge()
                    }

                    R.id.rb_outside_gujarat -> {
                        binding.rgCity.gone()
                        binding.tvCity.gone()
                        binding.etCity.setText("")
                        binding.etState.setText("")
                        calculateDeliveryCharge()
                    }
                }
            }
        }

        if (shippingChargeResponse.shipping_charge[0].gujarat_active == "yes") {
            binding.rbGujarat.visible()
        } else {
            binding.rbGujarat.gone()
        }

        if (shippingChargeResponse.shipping_charge[0].outofgujarat_active == "yes") {
            binding.rbOutsideGujarat.visible()
        } else {
            binding.rbOutsideGujarat.gone()
        }

        binding.rgCity.gone()
    }

    private fun handleResponse(addOrderResponse: AddOrderResponse?) {
        binding.btPlaceOrder.visible()
        binding.pbPlaceOrder.gone()
        if (null != addOrderResponse) {
            if (addOrderResponse.status.toInt() == 1) {
                startPayment(addOrderResponse.razorpay_orderid)
            } else {
                showSnackBar(addOrderResponse.message, this)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun areFieldsValid(): Boolean {
        if ((!binding.rbGujarat.isChecked) && (!binding.rbOutsideGujarat.isChecked)) {
            showSnackBar(getString(R.string.kindly_select_any_state), this)
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

        if (TextUtils.isEmpty(binding.etArea.text.toString())) {
            showSnackBar(getString(R.string.area_empty), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etSubArea.text.toString())) {
            showSnackBar(getString(R.string.sub_area_empty), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etDeliveryAddress.text.toString())) {
            showSnackBar(getString(R.string.invalid_address), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etZip.text.toString())) {
            showSnackBar(getString(R.string.invalid_zip), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etCity.text.toString())) {
            showSnackBar(getString(R.string.city_empty), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etState.text.toString())) {
            showSnackBar(getString(R.string.state_empty), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etAlternateMob.text.toString())) {
            showSnackBar(getString(R.string.alternate_mobile_no_empty), this)
            return false
        }

        if (binding.etAlternateMob.text.toString().length != 10) {
            showSnackBar(getString(R.string.alternate_invalid_mobile_no), this)
            return false
        }

        if (!binding.cbIAgree.isChecked) {
            showSnackBar(getString(R.string.kindly_accept_tnc), this)
            return false
        }
        return true
    }

    private fun showSuccessPaymentAlert() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(getString(R.string.payment_success_msg))
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

    private fun calculateDeliveryCharge() {
        var totalWeightInGrams = 0.0

        if (!getCartProductList().isNullOrEmpty()) {
            for (item in getCartProductList()) {
                if (item.packing_list[item.selectedItemPosition].weight_type == "GM") {
                    totalWeightInGrams += (item.packing_list[item.selectedItemPosition].product_weight.toDouble() * item.itemQuantity)
                } else if (item.packing_list[item.selectedItemPosition].weight_type == "KG") {
                    totalWeightInGrams += ((item.packing_list[item.selectedItemPosition].product_weight.toDouble() * 1000) * item.itemQuantity)
                }
            }
        }

        val weightInKg = if (totalWeightInGrams < 999) {
            1
        } else {
            (totalWeightInGrams / 1000).roundToInt()
        }

        if (binding.rbGujarat.isChecked && binding.rbRajkot.isChecked) {
            //gujarat, Rajkot
            //check minimum order value
            shippingCharge =
                if (totalAmount < shippingChargeResponse.shipping_charge[0].rajkot_min_amount.toDouble()) {
                    //Fix, shipping charge applicable
                    shippingChargeResponse.shipping_charge[0].rajkot_shipping.toDouble()
                } else {
                    //no shipping charge
                    0.0
                }
        } else if (binding.rbGujarat.isChecked && binding.rbOutsideRajkot.isChecked) {
            //gujarat, outside rajkot
            shippingCharge = shippingChargeResponse.shipping_charge[0].gujarat_shipping.toDouble()
            shippingCharge = (weightInKg * shippingCharge)
        } else if (binding.rbOutsideGujarat.isChecked) {
            //outside gujarat
            shippingCharge =
                shippingChargeResponse.shipping_charge[0].outof_gujarat_shipping.toDouble()
            shippingCharge = (weightInKg * shippingCharge)
        }

        binding.tvDeliveryChargeAmount.text =
            getString(R.string.total_rs, shippingCharge.toString())

        binding.tvGrandTotalAmount.text =
            getString(R.string.total_rs, (shippingCharge + totalAmount).toString())
    }

    private fun startPayment(razorpayOrderId: String) {
        val checkout = Checkout()
        checkout.setKeyID(AppConstants.RAZOR_PAY_ID)

        val activity: Activity = this
        try {
            val options = JSONObject()
            options.put("name", binding.etName.text.toString())
            options.put("description", getString(R.string.app_name))
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#C51C23")
            options.put("currency", "INR")
            options.put("order_id", razorpayOrderId)
            options.put(
                "amount",
                (totalAmount + shippingCharge).toString() + "00"
            )

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", binding.etEmail.text.toString())
            prefill.put("contact", binding.etMobile.text.toString())

            options.put("prefill", prefill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        println("Payment status: Success: $razorpayPaymentID")
        //todo call new API here
        showSuccessPaymentAlert()
    }

    override fun onPaymentError(code: Int, response: String?) {
        println("Payment status: Error: $response")
    }
}