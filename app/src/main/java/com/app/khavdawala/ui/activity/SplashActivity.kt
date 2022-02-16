package com.app.khavdawala.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.app.khavdawala.apputils.SPreferenceManager
import com.app.khavdawala.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var mCountDownTime = 200L //time in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            openHome()
        }, mCountDownTime)
    }

    private fun openHome() {
        if (SPreferenceManager.getInstance(this).isLogin) {
//            //startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        finish()
    }
}