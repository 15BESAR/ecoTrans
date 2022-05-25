package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.ResponseRegister
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    init {

    }

    fun postRegister(username: String, password: String, email: String, firstName: String, lastName: String, birthDate: String) {
        _isLoading.value = true
        _isError.value = false

        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        json.put("email", email)
        json.put("firstName", firstName)
        json.put("lastName", lastName)
        json.put("birthDate", birthDate+"T00:00:00Z")
        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        var client = ApiConfig.getApiService().register(requestBody)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: retrofit2.Response<ResponseRegister>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _errorMessage.value = response.body()?.status as String
                } else {
                    Log.e("RegisterActivity", "onFailure: ${response.message()}")

                    _errorMessage.value = response.message()
                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _errorMessage.value = t.message
                Log.e("RegisterActivity", "onFailure: ${t.message}")
            }
        })
    }
}