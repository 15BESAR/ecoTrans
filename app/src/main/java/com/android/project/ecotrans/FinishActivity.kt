package com.android.project.ecotrans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityFinishBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.FinishViewModel
import com.android.project.ecotrans.view_model.LoginViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding
    private lateinit var finishViewModel: FinishViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
//        setupAnimation()
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }

    private fun setupAction() {
        binding.btnGoBackToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setupViewModel() {
        finishViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[FinishViewModel::class.java]

        finishViewModel.isLoading.observe(this){
            showLoading(it)
        }

        finishViewModel.errorMessage.observe(this){
            showErrorMessage(it)
        }


    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun showLoading(it: Boolean) {
//        if (isLoading) {
//            binding.progressBarLogin.visibility = View.VISIBLE
//            binding.btnLogin.visibility = View.GONE
//            binding.textGoToSignUp.visibility = View.GONE
//        } else {
//            binding.progressBarLogin.visibility = View.GONE
//            binding.btnLogin.visibility = View.VISIBLE
//            binding.textGoToSignUp.visibility = View.VISIBLE
//        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@FinishActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}