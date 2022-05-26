package com.android.project.ecotrans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityPurchaseBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.PurchaseViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PurchaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPurchaseBinding
    private lateinit var purchaseViewModel: PurchaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val isDetailed:Boolean = intent.getBooleanExtra("isDetailed", false)

        setupView()
        setupViewModel(isDetailed)
//        setupAction()
//        setupAnimation()
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }
//
//    private fun setupAction() {
//        TODO("Not yet implemented")
//    }

    private fun setupViewModel(isDetailed: Boolean) {
        purchaseViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[PurchaseViewModel::class.java]

        purchaseViewModel.getUser().observe(this){
            if (!isDetailed){
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}