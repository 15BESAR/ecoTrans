package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.ResponseFinish
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback

class FinishViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _finishData = MutableLiveData<ResponseFinish>()
    var finishData: LiveData<ResponseFinish> = _finishData

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    init {
    }

    fun postJourney(token: String, requestBody: RequestBody) {
        _isLoading.value = true
        _isError.value = false

        var client = ApiConfig.getApiService().finishJourney("Bearer $token", requestBody)
        client.enqueue(object : Callback<ResponseFinish> {
            override fun onResponse(
                call: Call<ResponseFinish>,
                response: retrofit2.Response<ResponseFinish>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _finishData.value = response.body() as ResponseFinish
                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

                    _errorMessage.value = response.message()
                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseFinish>, t: Throwable) {

                _isError.value = true
                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}