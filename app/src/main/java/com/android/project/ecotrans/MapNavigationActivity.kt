package com.android.project.ecotrans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityLocationDetailBinding
import com.android.project.ecotrans.databinding.ActivityMapNavigationBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.LocationDetailViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapNavigationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var navMap: GoogleMap
    private lateinit var binding: ActivityMapNavigationBinding
    private lateinit var locationDetailViewModel: LocationDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        setupAction()
//        setupAnimation()
    }

    private fun setupViewModel() {
        mapNavigationViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[MapNavigationViewModel::class.java]

        mapNavigationViewModel.errorMessage.observe(this){
            showErrorMessage(it)
        }

        mapNavigationViewModel.isLoading.observe(this){
            showLoading(it)
        }

        mapNavigationViewModel.isLoadingJourneyInformation.observe(this){
            showLoadingJourneyInformation(it)
        }

        mapNavigationViewModel.isLoadingPreferenceList.observe(this){
            showLoadingPreferenceList(it)
        }
    }

    private fun setupView() {
        supportActionBar?.hide()

        val navMapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_mapNavigation_map) as SupportMapFragment
        navMapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupAction() {
        binding.btnDone.setOnClickListener {
            startActivity(Intent(this, FinishActivity::class.java))
        }
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }

    private fun showLoadingPreferenceList(it: Boolean?) {

    }

    private fun showLoadingJourneyInformation(it: Boolean?) {

    }

    private fun showLoading(it: Boolean?) {

    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@MapNavigationActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(navMap: GoogleMap) {
       this.navMap = navMap
    }
}