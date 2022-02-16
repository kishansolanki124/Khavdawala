package com.app.khavdawala.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.khavdawala.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    //private lateinit var signupViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        signupViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
//
//        signupViewModel.registerResponse().observe(this, Observer {
//            handleResponse(it)
//        })

        binding.btSubmitRegister.setOnClickListener {

            startActivity(Intent(this, HomeActivity::class.java))

//            if (areFieldsValid()) {
//                if (isConnected(this)) {
//                    pbRegister.visibility = View.VISIBLE
//                    btSubmitRegister.visibility = View.INVISIBLE
//                    signupViewModel.registerUser(
//                        RegisterRequest(
//                            etName.text.toString(),
//                            etCity.text.toString(),
//                            etContact.text.toString()
//                        )
//                    )
//                } else {
//                    showSnackBar(getString(R.string.no_internet), this)
//                }
//        }
        }
    }

//    private fun areFieldsValid(): Boolean {
//        if (TextUtils.isEmpty(etName.text.toString())) {
//            showSnackBar(getString(R.string.name_empty), this)
//            return false
//        }
//
//        if (TextUtils.isEmpty(etCity.text.toString())) {
//            showSnackBar(getString(R.string.city_empty), this)
//            return false
//        }
//
//        if (etCity.text.toString().length <= 2) {
//            showSnackBar(getString(R.string.invalid_city), this)
//            return false
//        }
//
//        if (TextUtils.isEmpty(etContact.text.toString())) {
//            showSnackBar(getString(R.string.mobile_no_empty), this)
//            return false
//        }
//
//        if (etContact.text.toString().length != 10) {
//            showSnackBar(getString(R.string.invalid_mobile_no), this)
//            return false
//        }
//
//        return true
//    }

//    private fun handleResponse(signupResponseModel: RegisterResponse?) {
//        if (null != signupResponseModel) {
//            println(signupResponseModel)
//            if (signupResponseModel.status_code == 1) {
//                SPreferenceManager.getInstance(this).saveSession(etContact.text.toString().trim())
//                startActivity(
//                    Intent(
//                        this,
//                        HomeActivity::class.java
//                    )
//                )
//                finish()
//            } else {
//                showSnackBar(signupResponseModel.msg, this)
//            }
//        } else {
//            showSnackBar(getString(R.string.something_went_wrong), this)
//            btSubmitRegister.visibility = View.VISIBLE
//            pbRegister.visibility = View.GONE
//        }
//    }
}