package com.android.project.ecotrans

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
//        binding.btnRegister.setOnClickListener {
//            var name = binding.myRegisterNameText.text
//            var email = binding.myRegisterEmailText.text
//            var password = binding.myRegisterPasswordText.text
//            var checkEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//            var checkPassword = password.toString().length >= 6
//            if (checkEmail && checkPassword){
//                registerViewModel.postRegister(name.toString(), email.toString(), password.toString())
//            }else{
//                Toast.makeText(this@RegisterActivity, "Unable to Register", Toast.LENGTH_SHORT).show()
//            }
//        }
        binding.gotosignin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.registerProgressBar.visibility = View.VISIBLE
//            binding.goLogin.visibility = View.GONE
//            binding.btnRegister.visibility = View.GONE
//        } else {
//            binding.registerProgressBar.visibility = View.GONE
//            binding.btnRegister.visibility = View.VISIBLE
//            binding.goLogin.visibility = View.VISIBLE
//        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@RegisterActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }


}