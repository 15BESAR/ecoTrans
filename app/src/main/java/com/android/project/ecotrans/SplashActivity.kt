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
import com.android.project.ecotrans.databinding.ActivitySplashBinding


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivitySplashBinding

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

        setupView()
        setupAnimation()
        setupAction()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            startActivity(intent)
            finish()
        }, 3000)
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