package com.android.project.ecotrans

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityLocationDetailBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.LocationDetailViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var detailMap: GoogleMap
    private lateinit var binding: ActivityLocationDetailBinding
    private lateinit var locationDetailViewModel: LocationDetailViewModel
    private lateinit var username: String

    private lateinit var destinationLocationLatLng: LatLng
    private var boundsBuilder = LatLngBounds.builder()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.destinationLocationLatLng = LatLng(-6.8770772, 107.6182631)
        this.username = "stevenss"

        setupViewModel()
        setupView()
        setupAction()
//        setupAnimation()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

        //this username
        locationDetailViewModel.getUser().observe(this){
            if (!it.username.isNullOrEmpty()){
                this.username = it.username
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()

        val detailMapFragment = supportFragmentManager.findFragmentById(R.id.fragment_locationDetail_map) as SupportMapFragment
        detailMapFragment.getMapAsync(this)
    }

    private fun setupAction() {
        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, MapNavigationActivity::class.java))
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
        Toast.makeText(this@LocationDetailActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(detailMap: GoogleMap) {
        this.detailMap = detailMap
        this.detailMap.clear()

        this.detailMap.uiSettings.isZoomControlsEnabled = true
        getOriginLocation()
        showDestinationLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getOriginLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getOriginLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOriginLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showMyMarker(location)
                } else {
                    Toast.makeText(
                        this@LocationDetailActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showMyMarker(location: Location) {
//        val originLocation = LatLng(location.latitude, location.longitude)
        val originLocation = LatLng(-6.8837471, 107.6163225)
        detailMap.addMarker(
            MarkerOptions()
                .position(originLocation)
                .title(this.username)
        )
        boundsBuilder.include(originLocation)

        detailMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 17f))
    }

    private fun showDestinationLocation() {
//        val destinationLocation = LatLng(this.destinationLocation.latitude, this.destinationLocation.longitude)
        detailMap.addMarker(
            MarkerOptions()
                .position(this.destinationLocationLatLng)
                .title("destination")
                .icon(getDestinationMarker())
        )?.showInfoWindow()

        detailMap.setOnMapLoadedCallback(OnMapLoadedCallback {
            boundsBuilder.include(this.destinationLocationLatLng)
            val bounds: LatLngBounds = boundsBuilder.build()
            detailMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 128))
        })
    }

    private fun getDestinationMarker(): BitmapDescriptor{
        var hsv: FloatArray = FloatArray(3)
        Color.colorToHSV(Color.parseColor("#558357"), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }
}