package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
import com.android.project.ecotrans.response.ResponseRoute
import com.android.project.ecotrans.response.StepsItem
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception
import kotlin.properties.Delegates

class LocationDetailViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading
    private var _desDetail = MutableLiveData<LatLng>()
    var desDetail: LiveData<LatLng> = _desDetail
    private var _routePoints = MutableLiveData<String>()
    var routePointes: LiveData<String> = _routePoints

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError
    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _desId = MutableLiveData<String>()
    var desId: LiveData<String> = _desId
    private var _oriId = MutableLiveData<String>()
    var oriId: LiveData<String> = _oriId
    private var _distance = MutableLiveData<Int>()
    var distance: LiveData<Int> = _distance
    private var _carbon = MutableLiveData<Int>()
    var carbon: LiveData<Int> = _carbon
    private var _reward = MutableLiveData<Int>()
    var reward: LiveData<Int> = _reward

    private var _timeEstimated = MutableLiveData<Int>()
    var timeEstimated: LiveData<Int> = _timeEstimated

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    init {
    }

    fun getRoutes(token: String, requestBody: RequestBody, preference: String) {
        _isLoading.value = true
        _isError.value = false

        var client = ApiConfig.getApiService().getRoutes("Bearer $token", requestBody)
        var tempLat: String
        var tempLng: String
        client.enqueue(object : Callback<ResponseRoute> {
            override fun onResponse(
                call: Call<ResponseRoute>,
                response: retrofit2.Response<ResponseRoute>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    try {
                        tempLat = response.body()?.routes?.get(0)?.legs?.get(0)?.endLocation?.lat.toString()
                        tempLng = response.body()?.routes?.get(0)?.legs?.get(0)?.endLocation?.lng.toString()
                        _desDetail.value = LatLng(tempLat.toDouble(), tempLng.toDouble())
                        _routePoints.value = response.body()?.routes?.get(0)?.overviewPolyline?.points as String

                        _oriId.value = response.body()?.geocodeWaypoints?.get(0)?.placeId as String
                        _desId.value = response.body()?.geocodeWaypoints?.get(1)?.placeId as String

                        var sumDistance: Int = 0
                        var sumEstimatedTime: Int = 0
                        var steps = response.body()?.routes?.get(0)?.legs?.get(0)?.steps as ArrayList<StepsItem>
                        for (item in steps){
                            sumDistance += item?.distance?.value as Int
                            sumEstimatedTime += item?.duration?.value as Int
                        }
                        _distance.value = sumDistance
//                        _distance.value = _distance.value?.div(1000.0)
                        _carbon.value = response.body()?.routes?.get(0)?.carbon as Int
                        _reward.value = response.body()?.routes?.get(0)?.reward as Int

                        _timeEstimated.value = sumEstimatedTime
                    } catch (e: Exception) {
                        _errorMessage.value = preference + " is not available"
                    }
                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

//                    _errorMessage.value = "Wrong Password or Email"
                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseRoute>, t: Throwable) {
//                _isLoading.value = false

//                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")

                _isError.value = true
            }
        })
    }
}