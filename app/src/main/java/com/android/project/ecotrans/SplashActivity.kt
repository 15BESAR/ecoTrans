package com.android.project.ecotrans

import android.R
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivitySplashBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.MainViewModel
import com.android.project.ecotrans.view_model.SplashViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val pref = SettingPreferences.getInstance(dataStore)
//        val configViewModel = ViewModelProvider(this, ConfigViewModelFactory(pref)).get(
//            ConfigViewModel::class.java
//        )
//        configViewModel.getThemeSettings().observe(this
//        ) { isDarkModeActive: Boolean ->
//            if (isDarkModeActive) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//            }
//        }

        setupViewModel()
        setupView()
        setupAnimation()
        setupAction()
    }

    private fun setupViewModel() {
        splashViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[SplashViewModel::class.java]

        splashViewModel.getUser().observe(this){
            if(it.isLogin){
                Handler().postDelayed({
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
                    startActivity(intent)
                    finish()
                }, 1500)
            }else{
                Handler().postDelayed({
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
                    startActivity(intent)
                    finish()
                }, 1500)
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {

    }

    private fun setupAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(750)
        val ecotrans = ObjectAnimator.ofFloat(binding.ecoTransSplashText, View.ALPHA, 1f).setDuration(750)

        AnimatorSet().apply {
            playSequentially(logo, ecotrans)
            start()
        }

    }
}