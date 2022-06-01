package com.android.project.ecotrans

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setupViewModel()
        setupView()
        setupAction()
        setupAnimation()
    }

    private fun setupViewModel() {

    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {

    }

    private fun setupAnimation() {

    }
}