package com.app.khavdawala.ui.activity

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.BuildConfig
import com.app.khavdawala.R
import com.app.khavdawala.apputils.SPreferenceManager
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.rateApp
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.databinding.ActivitySplashBinding
import com.app.khavdawala.pojo.response.ProductListResponse
import com.app.khavdawala.pojo.response.SettingsResponse
import com.app.khavdawala.viewmodel.SettingsViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var settingsViewModel: SettingsViewModel
    private var mCountDownTime = 2000L //time in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        fetchSettings()

        settingsViewModel.settingsResponse().observe(this) {
            handleResponse(it)
        }
    }

    private fun openHome() {
        if (SPreferenceManager.getInstance(this).isLogin) {
            //clear cart, todo here if need to not clear then remove this function
            clearCart()
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            val p1 = Pair.create(binding.ivLogo as View, "logo")
            val options =
                ActivityOptions.makeSceneTransitionAnimation(this, p1)
            startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
        }
        finish()
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

    private fun fetchSettings() {
        if (isConnected(this)) {
//            binding.pbSplash.visibility = View.VISIBLE
//            binding.ivSplash.visibility = View.GONE
            settingsViewModel.getSettings("")
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun handleResponse(settingsResponse: SettingsResponse?) {
//        binding.pbSplash.visibility = View.GONE
//        binding.ivSplash.visibility = View.VISIBLE

        if (null != settingsResponse) {
            SPreferenceManager.getInstance(this).saveSettings(settingsResponse)
            checkForUpdate(
                settingsResponse.settings[0].android_version,
                settingsResponse.settings[0].updatemsg,
                settingsResponse.settings[0].isfourceupdate
            )
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun checkForUpdate(apVersion: String, updateMsg: String, forceUpdate: String) {
        val versionCode: Int = BuildConfig.VERSION_CODE
        if (versionCode >= apVersion.toInt()) {
            Handler(Looper.getMainLooper()).postDelayed({
                openHome()
            }, mCountDownTime)
            return
        }

        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(updateMsg)
        if (forceUpdate == "no") {
            alertDialogBuilder.setCancelable(true)
            alertDialogBuilder.setNegativeButton(
                getString(android.R.string.cancel)
            ) { dialog, _ ->
                dialog.cancel()
            }
        } else {
            alertDialogBuilder.setCancelable(false)
        }

        alertDialogBuilder.setPositiveButton(
            getString(R.string.update_now)
        ) { dialog, _ ->
            dialog.cancel()
            rateApp()
            finish()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        if (forceUpdate == "no") {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.btnBG))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.btnBG))
        } else {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.btnBG))
        }
    }

}