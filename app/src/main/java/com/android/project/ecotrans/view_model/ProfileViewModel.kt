package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback

class ProfileViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoadingProfileData = MutableLiveData<Boolean>()
    var isLoadingProfileData: LiveData<Boolean> = _isLoadingProfileData

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _isDetailed = MutableLiveData<Boolean>()
    var isDetailed: LiveData<Boolean> = _isDetailed

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    init {
    }

    fun addProfileData(token: String, id: String, requestBody: RequestBody) {
        _isLoadingProfileData.value = true
        _isError.value = false
        _isDetailed.value = false

        var client = ApiConfig.getApiService().putuserdata("Bearer $token", id, requestBody)
        client.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: retrofit2.Response<User>
            ) {
                _isLoadingProfileData.value = false
                if (response.isSuccessful) {
                    _errorMessage.value = response.message() as String
                    _isDetailed.value = true
                } else {
                    Log.e("ProfileActivity", "onFailure: ${response.message()}")

                    _errorMessage.value = response.message() as String
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                _errorMessage.value = t.message as String
                _isLoadingProfileData.value = false
                Log.e("ProfileActivity", "onFailure: ${t.message}")
            }
        })
    }
}