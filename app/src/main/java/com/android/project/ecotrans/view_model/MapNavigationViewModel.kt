package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
import com.android.project.ecotrans.response.ResponseForecast
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class MapNavigationViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _temp = MutableLiveData<Double>()
    var temp: LiveData<Double> = _temp
    private var _uv = MutableLiveData<Double>()
    var uv: LiveData<Double> = _uv
    private var _aqi = MutableLiveData<Double>()
    var aqi: LiveData<Double> = _aqi

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError
    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    init {
    }

    fun getForecastData(token: String, requestBody: RequestBody) {
        _isLoading.value = true
        _isError.value = false

        var client = ApiConfig.getApiService().getForecast("Bearer $token", requestBody)
        client.enqueue(object : Callback<ResponseForecast> {
            override fun onResponse(
                call: Call<ResponseForecast>,
                response: retrofit2.Response<ResponseForecast>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {

                    _aqi.value = response.body()?.forecast?.aqi as Double
                    _temp.value = response.body()?.forecast?.temp as Double
                    _uv.value = response.body()?.forecast?.uv as Double

                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

//                    _errorMessage.value = "Wrong Password or Email"
//                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseForecast>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}