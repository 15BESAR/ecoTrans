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

        var client = ApiConfig.getApiService().putuserdata("Bearer $token", id, requestBody)
        client.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: retrofit2.Response<User>
            ) {
                _isLoadingProfileData.value = false
                if (response.isSuccessful) {

                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

//                    _errorMessage.value = "Wrong Password or Email"
//                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}