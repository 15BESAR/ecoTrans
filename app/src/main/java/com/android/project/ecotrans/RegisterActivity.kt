package com.android.project.ecotrans

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu_language, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.language){
//            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        setupAnimation()
    }

    private fun setupView() {
        supportActionBar?.hide()
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

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            var firstname = binding.editTextRegisterFirstname.text
            var lastname = binding.editTextRegisterLastname.text
            var username = binding.editTextRegisterUsername.text
            var date = binding.editTextRegisterDate.text
            var email = binding.editTextRegisterEmail.text
            var password = binding.editTextRegisterPassword.text

            var checkEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            var checkPassword = password.toString().length >= 6

            if (checkEmail && checkPassword){
                registerViewModel.postRegister(
//                    username.toString(),
//                    password.toString(),
//                    email.toString(),
//                    firstname.toString(),
//                    lastname.toString(),
//                    date.toString()
                    username.toString(), email.toString(), password.toString()
                )
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