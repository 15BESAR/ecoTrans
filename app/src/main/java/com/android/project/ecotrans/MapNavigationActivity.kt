package com.android.project.ecotrans

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityLocationDetailBinding
import com.android.project.ecotrans.databinding.ActivityMapNavigationBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.LocationDetailViewModel
import com.android.project.ecotrans.view_model.MapNavigationViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.concurrent.TimeUnit


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapNavigationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var navMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMapNavigationBinding
    private lateinit var mapNavigationViewModel: MapNavigationViewModel

    private var isTracking = false
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var originLocationLatLng: LatLng
    private lateinit var destinationLocationLatLng: LatLng
    private lateinit var username: String
    private var boundsBuilder = LatLngBounds.builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.destinationLocationLatLng = LatLng(-6.8770772, 107.6182631)
        this.username = "stevenss"

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
        getOriginLocation()
        createLocationRequest()
        createLocationCallback()

        this.navMap.uiSettings.isZoomControlsEnabled = true
        getOriginLocation()
        showDestinationLocation()
        showRoutes()

//        binding.btnDone.setOnClickListener {
//            if (!isTracking) {
//                updateTrackingStatus(true)
//                startLocationUpdates()
//            } else {
//                updateTrackingStatus(false)
//                stopLocationUpdates()
//            }
//        }
    }
    
    ////////////////////get my location

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
                        this@MapNavigationActivity,
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
//        navMap.addMarker(
//            MarkerOptions()
//                .position(originLocation)
//                .title(this.username)
//        )
        boundsBuilder.include(originLocation)

        navMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.destinationLocationLatLng, 17f))
        this.originLocationLatLng = originLocation
    }

    /////////////////// update my location

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Log.i(TAG, "onActivityResult: All location settings are satisfied.")
                RESULT_CANCELED ->
                    Toast.makeText(
                        this@MapNavigationActivity,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getOriginLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this@MapNavigationActivity, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(TAG, "onLocationResult: " + location.latitude + ", " + location.longitude)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(TAG, "Error : " + exception.message)
        }
    }
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    override fun onResume() {
        super.onResume()
        if (isTracking) {
            startLocationUpdates()
        }
    }
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    companion object {
        private const val TAG = "MapsActivity"
    }

    private fun updateTrackingStatus(newStatus: Boolean) {
        isTracking = newStatus
        if (isTracking) {
            binding.btnDone.text = getString(R.string.stop_running)
        } else {
            binding.btnDone.text = getString(R.string.start_running)
        }
    }
    
    
    /////////////////// routes

    private fun showDestinationLocation() {
//        val destinationLocation = LatLng(this.destinationLocation.latitude, this.destinationLocation.longitude)
        navMap.addMarker(
            MarkerOptions()
                .position(this.destinationLocationLatLng)
                .title("destination")
                .icon(getDestinationMarker())
        )?.showInfoWindow()

        navMap.setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback {
//            boundsBuilder.include(this.destinationLocationLatLng)
//            val bounds: LatLngBounds = boundsBuilder.build()
            navMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.originLocationLatLng, 20F))

            })
    }

    private fun showRoutes(){
        val path: List<LatLng>? = decodePoints("ln_i@_yyoS?LF@LB@BANMfAKnAGZM^KT?N@RFX]N_BmAyA_AoA}@w@i@]OmAWcDm@qAUiF}@wCi@_@KeCiBwBqAv@mANC")

        if (path != null) {
            var polylineOptions: PolylineOptions = PolylineOptions()
            polylineOptions.addAll(path)
            polylineOptions.width(20F)
            polylineOptions.color(R.color.purple_700)
            polylineOptions.pattern(listOf(Dot(), Gap(15F)))
            navMap.addPolyline(polylineOptions)
        }
    }

    private fun getDestinationMarker(): BitmapDescriptor {
        var hsv: FloatArray = FloatArray(3)
        Color.colorToHSV(Color.parseColor("#558357"), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    fun decodePoints(points: String): List<LatLng>? {
        val len = points.length
        var index = 0
        val decoded: MutableList<LatLng> = ArrayList()
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = points[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = points[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            decoded.add(
                LatLng(
                    lat / 100000.0,
                    lng / 100000.0
                )
            )
        }
        return decoded
    }
}