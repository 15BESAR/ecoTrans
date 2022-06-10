package com.android.project.ecotrans

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
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
import com.android.project.ecotrans.databinding.ActivityLocationDetailBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.view_model.LocationDetailViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var detailMap: GoogleMap
    private lateinit var binding: ActivityLocationDetailBinding
    private lateinit var locationDetailViewModel: LocationDetailViewModel
    private lateinit var username: String

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var token: String
    private lateinit var id: String
    private lateinit var destinationName: String
    private lateinit var originLocation: String
    private var preference: String = "walking"
    private lateinit var pointes: String
    private lateinit var polyline: Polyline
    private var timeEstimated by Delegates.notNull<Int>()

    private lateinit var destinationId: String
    private lateinit var originId: String
    private var distance by Delegates.notNull<Int>()
    private var carbon by Delegates.notNull<Int>()
    private var reward by Delegates.notNull<Int>()

    private lateinit var destinationLocationLatLng: LatLng
    private var boundsBuilder = LatLngBounds.builder()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.destinationName = intent.getStringExtra("location") as String

//        this.destinationLocationLatLng = LatLng(-6.8770772, 107.6182631)
//        this.username = "stevenss"

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
            this.polyline.remove()
            binding.btnStart.visibility = View.GONE
            showDestinationLocation()
        }

        locationDetailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        //this username
        locationDetailViewModel.getUser().observe(this){
            if (!it.username.isNullOrEmpty()){
                this.username = it.username
                this.id = it.id
                this.token = it.token
            }
        }

        //desDetail latlng achieved
        locationDetailViewModel.desDetail.observe(this){
            if(it != null){
                this.destinationLocationLatLng = it
                locationDetailViewModel.routePointes.observe(this){ point ->
                    if(point != null){
                        this.pointes = point
                        showDestinationLocation()
                    }
                }
            }
        }

        //set finish parameter
        locationDetailViewModel.oriId.observe(this){
            if (it != null){
                this.originId = it
            }
        }
        locationDetailViewModel.desId.observe(this){
            if (it != null){
                this.destinationId = it
            }
        }
        locationDetailViewModel.carbon.observe(this){
            if (it != null){
                this.carbon = it
            }
        }
        locationDetailViewModel.reward.observe(this){
            if (it != null){
                this.reward = it
            }
        }
        locationDetailViewModel.distance.observe(this){
            if (it != null){
                this.distance = it
            }
        }

        //set timeEstimated
        locationDetailViewModel.timeEstimated.observe(this){
            if (it != null){
                this.timeEstimated = it
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
            stopLocationUpdates()

            val intent = Intent(this, MapNavigationActivity::class.java)
            intent.putExtra("destinationLat", this.destinationLocationLatLng.latitude.toString())
            intent.putExtra("destinationLng", this.destinationLocationLatLng.longitude.toString())
            intent.putExtra("pointes", this.pointes)

            intent.putExtra("originId", this.originId)
            intent.putExtra("destinationId", this.destinationId)
            intent.putExtra("distance", this.distance)
            intent.putExtra("carbon", this.carbon)
            intent.putExtra("reward", this.reward)

            intent.putExtra("timeEstimated", this.timeEstimated)
            intent.putExtra("destinationName", this.destinationName)
            intent.putExtra("token", this.token)
            startActivity(intent)
        }

        binding.imageViewLocationDetailBack.setOnClickListener {
            finish()
        }

        binding.constraintLayoutLocatoinDetailPreference1.setOnClickListener {
            binding.constraintLayoutLocatoinDetailPreference1.setBackgroundResource(R.drawable.itemclicked_bg)
            this.preference = "walking"
            binding.constraintLayoutLocatoinDetailPreference2.setBackgroundResource(R.drawable.item_bg)
            binding.constraintLayoutLocatoinDetailPreference3.setBackgroundResource(R.drawable.item_bg)
            val json = JSONObject()
            json.put("userId", this.id)
            json.put("origin", this.originLocation)
            json.put("destination", this.destinationName)//"Jalan Dago Asri, Dago, Kota Bandung, Jawa Barat, Indonesia")
            json.put("preference", this.preference)
            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            detailMap.clear()
            getOriginLocation()
            locationDetailViewModel.getRoutes(this.token, requestBody, this.preference)
            if(this.destinationLocationLatLng != null){
                showRoutes()
            }
        }
        binding.constraintLayoutLocatoinDetailPreference2.setOnClickListener {
            binding.constraintLayoutLocatoinDetailPreference2.setBackgroundResource(R.drawable.itemclicked_bg)
            this.preference = "bicycling"
            binding.constraintLayoutLocatoinDetailPreference1.setBackgroundResource(R.drawable.item_bg)
            binding.constraintLayoutLocatoinDetailPreference3.setBackgroundResource(R.drawable.item_bg)
            val json = JSONObject()
            json.put("userId", this.id)
            json.put("origin", this.originLocation)
            json.put("destination", this.destinationName)
            json.put("preference", this.preference)
            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            detailMap.clear()
            getOriginLocation()
            locationDetailViewModel.getRoutes(this.token, requestBody, this.preference)
            if(this.destinationLocationLatLng != null){
                showRoutes()
            }
        }
        binding.constraintLayoutLocatoinDetailPreference3.setOnClickListener {
            binding.constraintLayoutLocatoinDetailPreference3.setBackgroundResource(R.drawable.itemclicked_bg)
            this.preference = "transit"
            binding.constraintLayoutLocatoinDetailPreference1.setBackgroundResource(R.drawable.item_bg)
            binding.constraintLayoutLocatoinDetailPreference2.setBackgroundResource(R.drawable.item_bg)
            val json = JSONObject()
            json.put("userId", this.id)
            json.put("origin", this.originLocation)
            json.put("destination", this.destinationName)
            json.put("preference", this.preference)
            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            detailMap.clear()
            getOriginLocation()
            locationDetailViewModel.getRoutes(this.token, requestBody, this.preference)
            if(this.destinationLocationLatLng != null){
                showRoutes()
            }
        }
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }

    private fun showLoading(it: Boolean?) {
        if (it == true) {
            binding.progressBarDetailLocation.visibility = View.VISIBLE
            binding.btnStart.visibility = View.GONE
        } else {
            binding.progressBarDetailLocation.visibility = View.GONE
            binding.btnStart.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@LocationDetailActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(detailMap: GoogleMap) {
        this.detailMap = detailMap
        this.detailMap.clear()
        setMapStyle()
        createLocationCallback()
        createLocationRequest()


        this.detailMap.uiSettings.isZoomControlsEnabled = true
//        getOriginLocation()
        startLocationUpdates()
    }


    private fun setMapStyle() {
        try {
            val success =
                detailMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "LocationDetailActivity"
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
        this.originLocation = location.latitude.toString() + "," + location.longitude.toString()
        val originLocation = LatLng(location.latitude, location.longitude)
        detailMap.addMarker(
            MarkerOptions()
                .position(originLocation)
                .title(this.username)
        )
        boundsBuilder.include(originLocation)

        detailMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 17f))

        val json = JSONObject()
        json.put("userId", this.id)
        json.put("origin", this.originLocation)
        json.put("destination", this.destinationName)
        json.put("preference", this.preference)
        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        locationDetailViewModel.getRoutes(this.token, requestBody, this.preference)
    }

    private fun showDestinationLocation() {
//        val destinationLocation = LatLng(this.destinationLocation.latitude, this.destinationLocation.longitude)
        detailMap.addMarker(
            MarkerOptions()
                .position(this.destinationLocationLatLng)
                .title(this.destinationName)
                .icon(getDestinationMarker())
        )//?.showInfoWindow()

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

    private fun showRoutes(){

        val path: List<LatLng>? = decodePoints(this.pointes)//decodePoints(ln_i@_yyoS?LF@LB@BANMfAKnAGZM^KT?N@RFX]N_BmAyA_AoA}@w@i@]OmAWcDm@qAUiF}@wCi@_@KeCiBwBqAv@mANC")//decodePoints("ln_i@_yyoS?LF@LB@BANMfAKnAGZM^KT?N@RFX]N_BmAyA_AoA}@w@i@]OmAWcDm@qAUiF}@wCi@_@KeCiBwBqAv@mANC")

        if (path != null) {
            var polylineOptions: PolylineOptions = PolylineOptions()
            polylineOptions.addAll(path)
            polylineOptions.width(20F)
            polylineOptions.color(R.color.new_teal_2)
            polylineOptions.pattern(listOf(Dot(), Gap(10F)))
            this.polyline = detailMap.addPolyline(polylineOptions)
        }
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
                        this@LocationDetailActivity,
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
                        Toast.makeText(this@LocationDetailActivity, sendEx.message, Toast.LENGTH_SHORT).show()
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

//                    draw polyline
//                    allLatLng.add(lastLatLng)
//                    navMap.addPolyline(
//                        PolylineOptions()
//                            .color(Color.CYAN)
//                            .width(10f)
//                            .addAll(allLatLng))

//                    val cameraPosition = CameraPosition.Builder()
//                        .target(lastLatLng)
//                        .zoom(17F)
//                        .build()
//                    val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
//                    navMap.animateCamera(cameraUpdate)
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
//    override fun onResume() {
//        super.onResume()
////        startLocationUpdates()
//    }
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}