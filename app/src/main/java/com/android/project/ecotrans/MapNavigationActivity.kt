package com.android.project.ecotrans

import android.Manifest
import android.R.attr.strokeColor
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityMapNavigationBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.MapNavigationViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


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

    private var boundsBuilder = LatLngBounds.builder()
    private lateinit var pointes: String
    private lateinit var polyline: Polyline

    private lateinit var token: String
    private lateinit var destinationName: String

    private lateinit var destinationId: String
    private lateinit var originId: String
    private var distance by Delegates.notNull<Int>()
    private var carbon by Delegates.notNull<Int>()
    private var reward by Delegates.notNull<Int>()

    private var timeEstimated by Delegates.notNull<Int>()

    private var isFinished: Boolean = false

    //test radius
    private lateinit var destinationCircle: Circle
    private var inOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        this.destinationLocationLatLng = LatLng(-6.8770772, 107.6182631)
//        this.username = "stevenss"
        val desLat = intent.getStringExtra("destinationLat") as String
        val desLng = intent.getStringExtra("destinationLng") as String
        this.pointes = intent.getStringExtra("pointes") as String
        this.destinationLocationLatLng = LatLng(desLat.toDouble(), desLng.toDouble())

        this.originId = intent.getStringExtra("originId") as String
        this.destinationId = intent.getStringExtra("destinationId") as String
        this.distance = intent.getIntExtra("distance", 0)
        this.carbon = intent.getIntExtra("carbon", 0)
        this.reward = intent.getIntExtra("reward", 0)

        this.token = intent.getStringExtra("token") as String
        this.destinationName = intent.getStringExtra("destinationName") as String

        this.timeEstimated = intent.getIntExtra("timeEstimated", 0)

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

        //forcastin values
        mapNavigationViewModel.aqi.observe(this){
            if(it != null){
                binding.textViewMapNavigationAqiValue.text = it.toString()
            }
        }
        mapNavigationViewModel.uv.observe(this){
            if(it != null){
                binding.textViewMapNavigationUVValue.text = it.toString()
            }
        }
        mapNavigationViewModel.temp.observe(this){
            if(it != null){
                binding.textViewMapNavigationTempratureValue.text = it.toString()
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()

        val navMapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_mapNavigation_map) as SupportMapFragment
        navMapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val json = JSONObject()
        json.put("destination", this.destinationName)
        json.put("arrivedHour", this.timeEstimated/3600)
        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        mapNavigationViewModel.getForecastData(this.token, requestBody)
    }

    private fun setupAction() {
//        binding.btnDone.setOnClickListener {
//            startActivity(Intent(this, FinishActivity::class.java))
//        }

        binding.imageViewMapNavigationBack.setOnClickListener {
            finish()
        }
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }

    private fun showLoading(it: Boolean?) {
        if (it == true) {
            binding.progressBarMapNavigationForcasting.visibility = View.VISIBLE
//            binding.btnStart.visibility = View.GONE
            binding.textViewMapNavigationAqiValue.visibility = View.GONE
            binding.textViewMapNavigationUVValue.visibility = View.GONE
            binding.textViewMapNavigationTempratureValue.visibility = View.GONE
        } else {
            binding.progressBarMapNavigationForcasting.visibility = View.GONE
//            binding.btnStart.visibility = View.VISIBLE
            binding.textViewMapNavigationAqiValue.visibility = View.VISIBLE
            binding.textViewMapNavigationUVValue.visibility = View.VISIBLE
            binding.textViewMapNavigationTempratureValue.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@MapNavigationActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(navMap: GoogleMap) {
        isFinished = false
        this.navMap = navMap
        createLocationRequest()
        createLocationCallback()
        setMapStyle()

        this.navMap.uiSettings.isZoomControlsEnabled = true
        getOriginLocation()
        showDestinationLocation()
        showRoutes()
        startLocationUpdates()

        navMap.setOnMyLocationChangeListener(OnMyLocationChangeListener { radius ->
            val distance = FloatArray(2)
            Location.distanceBetween(
                radius.latitude, radius.longitude,
            destinationCircle.getCenter().latitude, destinationCircle.getCenter().longitude, distance
            )

            if (distance[0] <= destinationCircle.getRadius() && !inOnce) {
                inOnce = true
                isFinished = true
                stopLocationUpdates()
                val intent = Intent(this, FinishActivity::class.java)
                intent.putExtra("isFinished", this.isFinished)

                intent.putExtra("originId", this.originId)
                intent.putExtra("destinationId", this.destinationId)
                intent.putExtra("distance", this.distance)
                intent.putExtra("carbon", this.carbon)
                intent.putExtra("reward", this.reward)

                startActivity(intent)
            }
        })
    }

    private fun setMapStyle() {
        try {
            val success =
                navMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
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
                    navMap.isMyLocationEnabled = true
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

        val originLocation = LatLng(location.latitude, location.longitude)
//        val originLocation = LatLng(-6.8837471, 107.6163225)
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
                    val lastLatLng = LatLng(location.latitude, location.longitude)

                    //draw polyline
//                    allLatLng.add(lastLatLng)
//                    navMap.addPolyline(
//                        PolylineOptions()
//                            .color(Color.CYAN)
//                            .width(10f)
//                            .addAll(allLatLng))

                    val cameraPosition = CameraPosition.Builder()
                        .target(lastLatLng)
                        .zoom(17F)
                        .build()
                    val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
                    navMap.animateCamera(cameraUpdate)
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

//    private fun updateTrackingStatus(newStatus: Boolean) {
//        isTracking = newStatus
//        if (isTracking) {
//            binding.btnDone.text = getString(R.string.stop_running)
//        } else {
//            binding.btnDone.text = getString(R.string.start_running)
//        }
//    }
    
    
    /////////////////// routes

    private fun showDestinationLocation() {
//        val destinationLocation = LatLng(this.destinationLocation.latitude, this.destinationLocation.longitude)
        navMap.addMarker(
            MarkerOptions()
                .position(this.destinationLocationLatLng)
                .title("destination")
                .icon(getDestinationMarker())
        )?.showInfoWindow()

        destinationCircle = navMap.addCircle(
            CircleOptions()
                .center(this.destinationLocationLatLng)
                .radius(50.0)
                .fillColor(0x22FF0000)
                .strokeColor(Color.RED)
                .strokeWidth(3f)
        )

        navMap.setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback {
//            boundsBuilder.include(this.destinationLocationLatLng)
//            val bounds: LatLngBounds = boundsBuilder.build()
            navMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.originLocationLatLng, 20F))

            })
    }

    private fun showRoutes(){
        val path: List<LatLng>? = decodePoints(this.pointes)

        if (path != null) {
            var polylineOptions: PolylineOptions = PolylineOptions()
            polylineOptions.addAll(path)
            polylineOptions.width(20F)
            polylineOptions.color(R.color.new_teal_2)
            polylineOptions.pattern(listOf(Dot(), Gap(15F)))
            this.polyline = navMap.addPolyline(polylineOptions)
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