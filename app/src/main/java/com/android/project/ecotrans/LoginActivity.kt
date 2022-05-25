package com.android.project.ecotrans

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityLoginBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.LoginViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu_language, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.language){
//            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        loginViewModel.errorMessage.observe(this) {
            showErrorMessage(it)
        }
        loginViewModel.getUser().observe(this) { user ->
            if(user.isLogin){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            var username = binding.editTextLoginUsername.text
            var password = binding.editTextLoginPassword.text

            var checkEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()
            var checkPassword = password.toString().length >= 6

            if (checkEmail && checkPassword){
                loginViewModel.postLogin(username.toString(), password.toString())
            }else{
                Toast.makeText(this@LoginActivity, "Unable to Login", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textGoToSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
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
            binding.progressBarLogin.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
            binding.textGoToSignUp.visibility = View.GONE
        } else {
            binding.progressBarLogin.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
            binding.textGoToSignUp.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@LoginActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }


}