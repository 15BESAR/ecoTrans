package com.android.project.ecotrans

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.project.ecotrans.databinding.ActivityProfileBinding
import com.android.project.ecotrans.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

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