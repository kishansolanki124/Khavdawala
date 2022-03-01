package com.app.khavdawala.ui.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.khavdawala.R
import com.app.khavdawala.apputils.SPreferenceManager
import com.app.khavdawala.apputils.isConnected
import com.app.khavdawala.apputils.showSnackBar
import com.app.khavdawala.databinding.ActivityRegisterBinding
import com.app.khavdawala.pojo.response.RegisterResponse
import com.app.khavdawala.pojo.request.RegisterRequest
import com.app.khavdawala.viewmodel.RegisterViewModel
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var signupViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textWatcherBirthDate()

        signupViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        signupViewModel.registerResponse().observe(this) {
            handleResponse(it)
        }

        binding.btSubmitRegister.setOnClickListener {

            if (areFieldsValid()) {
                if (isConnected(this)) {
                    binding.pbRegister.visibility = View.VISIBLE
                    binding.btSubmitRegister.visibility = View.INVISIBLE
                    signupViewModel.registerUser(
                        RegisterRequest(
                            binding.etName.text.toString(),
                            binding.etMob.text.toString(),
                            binding.etBirthDate.text.toString()
                        )
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet), this)
                }
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        if (TextUtils.isEmpty(binding.etName.text.toString())) {
            showSnackBar(getString(R.string.name_empty), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etMob.text.toString())) {
            showSnackBar(getString(R.string.mobile_no_empty), this)
            return false
        }

        if (binding.etMob.text.toString().length != 10) {
            showSnackBar(getString(R.string.invalid_mobile_no), this)
            return false
        }

        if (TextUtils.isEmpty(binding.etBirthDate.text.toString())) {
            showSnackBar(getString(R.string.bdate_no_empty), this)
            return false
        }

        return true
    }

    private fun handleResponse(signupResponseModel: RegisterResponse?) {
        if (null != signupResponseModel) {
            println(signupResponseModel)
            if (signupResponseModel.status.toInt() == 1) {
                SPreferenceManager.getInstance(this)
                    .saveSession(binding.etMob.text.toString().trim())
                startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )
                finish()
            } else {
                showSnackBar(signupResponseModel.message, this)
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
            binding.btSubmitRegister.visibility = View.VISIBLE
            binding.pbRegister.visibility = View.GONE
        }
    }

    private fun textWatcherBirthDate() {
        val date =
            OnDateSetListener { _, year, month, day ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = month
                myCalendar[Calendar.DAY_OF_MONTH] = day
                updateLabel()
            }

        binding.etBirthDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this@RegisterActivity,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )

            datePickerDialog.datePicker.maxDate = (Date().time - 86400000)
            datePickerDialog.show()
        }
    }

    private fun updateLabel() {
        val myFormat = "dd-MM-yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etBirthDate.setText(dateFormat.format(myCalendar.time))
    }
}