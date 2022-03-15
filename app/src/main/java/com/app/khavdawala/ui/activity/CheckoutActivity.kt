package com.app.khavdawala.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.*
import com.app.khavdawala.databinding.ActivityCheckoutBinding
import com.app.khavdawala.pojo.request.AddOrderStatusRequest
import com.app.khavdawala.pojo.request.OrderPlaceRequest
import com.app.khavdawala.pojo.response.*
import com.app.khavdawala.viewmodel.OrderViewModel
import com.app.khavdawala.viewmodel.StaticPageViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var shippingChargeResponse: ShippingChargeResponse
    private var totalAmount = 0.0
    private var shippingCharge = 0.0
    private var orderNo = ""
    private var razorPayOrderId = ""
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var staticPageViewModel: StaticPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialising RazorPay SDK
        Checkout.preload(this)

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        staticPageViewModel = ViewModelProvider(this)[StaticPageViewModel::class.java]

        orderViewModel.registerResponse().observe(this) {
            handleResponse(it)
        }

        orderViewModel.addOrderStatusResponse().observe(this) {
            handleOrderStatusResponse(it)
        }

        staticPageViewModel.categoryResponse().observe(this) {
            handleStaticPageResponse(it)
        }

        orderViewModel.shippingChargeResponse().observe(this) {
            handleShippingChargeResponse(it)
        }

        orderViewModel.addressResponse().observe(this) {
            handleAddressResponse(it)
        }

        totalAmount = intent.getDoubleExtra(AppConstants.AMOUNT, 0.0)

        binding.tvTotalAmount.text = rupeesWithTwoDecimal(totalAmount)
        binding.tvGrandTotalAmount.text =
            rupeesWithTwoDecimal(shippingCharge + totalAmount)
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
                            productId += item.product_id + ","
                            productName += item.name + ","
                            packingId += item.cartPackingId + ","
                            packingWeight +=
                                item.packing_list[item.selectedItemPosition].product_weight + ","
                            packingWeightType +=
                                item.packing_list[item.selectedItemPosition].weight_type + ","
                            packingQuantity += item.itemQuantity.toString() + ","
                            packingPrice +=
                                item.packing_list[item.selectedItemPosition].product_price + ","
                        }

                        productId = productId.removeLastComma()
                        productName = productName.removeLastComma()
                        packingId = packingId.removeLastComma()
                        packingWeight = packingWeight.removeLastComma()
                        packingWeightType = packingWeightType.removeLastComma()
                        packingQuantity = packingQuantity.removeLastComma()
                        packingPrice = packingPrice.removeLastComma()
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

        binding.tvDeliveryChargeAmount.text = rupeesWithTwoDecimal(0.0)
        binding.llCheckoutDetails.gone()

        highlightTNC()
    }

    private fun handleOrderStatusResponse(registerResponse: RegisterResponse?) {
        binding.btPlaceOrder.visible()
        binding.pbPlaceOrder.gone()
        if (null != registerResponse) {
            if (registerResponse.status == "1") {
                clearCart()
                //finish all previous activities
                val intent = Intent(this, OrderSuccessActivity::class.java)
                    .putExtra(AppConstants.orderId, orderNo)
                    .putExtra(AppConstants.paymentId, razorPayOrderId)
                    .putExtra(
                        AppConstants.totalPaidAmount,
                        (totalAmount + shippingCharge).toString()
                    )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                //showSuccessPaymentAlert()
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }

    }

    private fun handleStaticPageResponse(staticPageResponse: StaticPageResponse?) {
        binding.tvTnc.visible()
        binding.pbTnc.gone()
        if (null != staticPageResponse) {
            if (staticPageResponse.status == "1") {
                for (item in staticPageResponse.staticpage) {
                    if (item.name.contains("Terms")) {
                        showTNCDialog(item.description)
                    }
                }
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun showTNCDialog(description: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.tnc_alert, null)
        dialogBuilder.setView(dialogView)

        val tncTextView = dialogView.findViewById<View>(R.id.tv_html) as TextView
        val ivClose = dialogView.findViewById<View>(R.id.iv_close) as ImageView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tncTextView.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tncTextView.text = Html.fromHtml(description)
        }

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.show()

        ivClose.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun highlightTNC() {
        binding.tvTnc.setOnClickListener {
            if (isConnected(this)) {
                binding.pbTnc.visible()
                binding.tvTnc.invisible()
                staticPageViewModel.getStaticPage()
            } else {
                showSnackBar(getString(R.string.no_internet), this)
            }
        }
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
                orderNo = addOrderResponse.order_no
                razorPayOrderId = addOrderResponse.razorpay_orderid
                startPayment(addOrderResponse.keyId)
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

//        if (TextUtils.isEmpty(binding.etAlternateMob.text.toString())) {
//            showSnackBar(getString(R.string.alternate_mobile_no_empty), this)
//            return false
//        }

//        if (binding.etAlternateMob.text.toString().length != 10) {
//            showSnackBar(getString(R.string.alternate_invalid_mobile_no), this)
//            return false
//        }

        if (!binding.cbIAgree.isChecked) {
            showSnackBar(getString(R.string.kindly_accept_tnc), this)
            return false
        }
        return true
    }

//    private fun showSuccessPaymentAlert() {
//        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
//            .setTitle(getString(R.string.app_name))
//            .setMessage(getString(R.string.payment_success_msg))
//            .setCancelable(false)
//            .setPositiveButton(resources.getString(android.R.string.ok)) { dialog, _ ->
//                dialog.dismiss()
//                clearCart()
//
//                //finish all previous activities
//                val intent = Intent(this, HomeActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                startActivity(intent)
//            }
//            .show()
//    }

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
            //(totalWeightInGrams / 1000).roundToInt()
            (totalWeightInGrams / 1000)
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
            shippingCharge = (weightInKg.toDouble() * shippingCharge)
        } else if (binding.rbOutsideGujarat.isChecked) {
            //outside gujarat
            shippingCharge =
                shippingChargeResponse.shipping_charge[0].outof_gujarat_shipping.toDouble()
            shippingCharge = (weightInKg.toDouble() * shippingCharge)
        }

        binding.tvDeliveryChargeAmount.text =
            rupeesWithTwoDecimal(shippingCharge)

        binding.tvGrandTotalAmount.text =
            rupeesWithTwoDecimal(shippingCharge + totalAmount)
    }

    private fun startPayment(keyId: String) {
        val checkout = Checkout()
        checkout.setKeyID(keyId)

        val activity: Activity = this
        try {
            val options = JSONObject()
            options.put("name", getString(R.string.app_name))
            options.put(
                "description",
                "Order From: " + binding.etName.text.toString() + ", Order No.: " + orderNo
            )
            options.put("image", "https://khavdawala.com/images/rpay_logo.png")
            options.put("theme.color", "#C51C23")
            options.put("currency", "INR")
            options.put("order_id", razorPayOrderId)
            options.put("merchant_order_id", orderNo)
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
            prefill.put("merchant_order_id", orderNo)
            options.put("prefill", prefill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        println("Payment status: Success: $razorpayPaymentID")
        binding.btPlaceOrder.invisible()
        binding.pbPlaceOrder.visible()
        orderViewModel.addOrderStatus(
            AddOrderStatusRequest(
                orderNo,
                razorPayOrderId,
                razorpayPaymentID.toString()
            )
        )
    }

    override fun onPaymentError(code: Int, response: String?) {
        showToast("Payment status: Error: $response")
    }
}