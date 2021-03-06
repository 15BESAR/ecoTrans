package com.android.project.ecotrans

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityRegisterBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.RegisterViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        setupAction()
        setupAnimation()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this){
            showLoading(it)
        }
        registerViewModel.errorMessage.observe(this){
            showErrorMessage(it)
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
        binding.autoCompleteTextViewRegisterDay.setAdapter(ArrayAdapter(this, R.layout.dropdown_date_item, resources.getStringArray(R.array.day)))
        binding.autoCompleteTextViewRegisterMonth.setAdapter(ArrayAdapter(this, R.layout.dropdown_date_item, resources.getStringArray(R.array.month)))
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            var firstname = binding.editTextRegisterFirstname.text
            var lastname = binding.editTextRegisterLastname.text
            var username = binding.editTextRegisterUsername.text

            var intMonth: String = String()

            if (binding.autoCompleteTextViewRegisterMonth.text.toString() == "January"){
                intMonth = "01"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() == "February"){
                intMonth = "02"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="March"){
                intMonth = "03"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="April"){
                intMonth = "04"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="May"){
                intMonth = "05"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="June"){
                intMonth = "06"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="July"){
                intMonth = "07"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="August"){
                intMonth = "08"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="September"){
                intMonth = "09"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="October"){
                intMonth = "10"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="November"){
                intMonth = "11"
            }else if (binding.autoCompleteTextViewRegisterMonth.text.toString() =="December"){
                intMonth = "12"
            }

            var date = binding.autoCompleteTextViewRegisterYear.text.toString() + "-" +
                    intMonth.toString() + "-" +
                    binding.autoCompleteTextViewRegisterDay.text.toString()

            var email = binding.editTextRegisterEmail.text
            var password = binding.editTextRegisterPassword.text

            var checkEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            var checkPassword = password.toString().length >= 6

            if (checkEmail && checkPassword){
                registerViewModel.postRegister(
                    username.toString(),
                    password.toString(),
                    email.toString(),
                    firstname.toString(),
                    lastname.toString(),
                    date
                )
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                Toast.makeText(this@RegisterActivity, "Unable to Register", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textGoToSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupAnimation() {
//        ObjectAnimator.ofFloat(binding.logoGit, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 4000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarRegister.visibility = View.VISIBLE
            binding.textGoToSignIn.visibility = View.GONE
            binding.btnRegister.visibility = View.GONE
        } else {
            binding.progressBarRegister.visibility = View.GONE
            binding.textGoToSignIn.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@RegisterActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}