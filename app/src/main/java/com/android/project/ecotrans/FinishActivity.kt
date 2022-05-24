package com.android.project.ecotrans

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        setupView()
        setupViewModel()
        setupAction()
        setupAnimation()
    }

    private fun setupAnimation() {
        TODO("Not yet implemented")
    }

    private fun setupAction() {
        TODO("Not yet implemented")
    }

    private fun setupViewModel() {
        TODO("Not yet implemented")
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}