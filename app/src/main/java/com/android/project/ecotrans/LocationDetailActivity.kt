package com.android.project.ecotrans

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityLocationDetailBinding
import com.android.project.ecotrans.databinding.ActivityMainBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.LocationDetailViewModel
import com.android.project.ecotrans.view_model.MainViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LocationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationDetailBinding
    private lateinit var locationDetailViewModel: LocationDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailBinding.inflate(layoutInflater)
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
//        TODO("Not yet implemented")
    }

    private fun setupViewModel() {
        locationDetailViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[LocationDetailViewModel::class.java]

        locationDetailViewModel.errorMessage.observe(this){
            showErrorMessage(it)
        }

        locationDetailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        locationDetailViewModel.isLoadingJourneyInformation.observe(this){
            showLoadingJourneyInformation(it)
        }

        locationDetailViewModel.isLoadingPreferenceList.observe(this){
            showLoadingPreferenceList(it)
        }
    }

    private fun showLoadingPreferenceList(it: Boolean?) {

    }

    private fun showLoadingJourneyInformation(it: Boolean?) {

    }

    private fun showLoading(it: Boolean?) {

    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@LocationDetailActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}