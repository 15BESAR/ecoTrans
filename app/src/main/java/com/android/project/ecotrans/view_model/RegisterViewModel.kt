package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.project.ecotrans.model.UserPreference
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

    fun postRegister(name: String, email: String, password: String) {
//        _isLoading.value = true
//        _isError.value = false
//        var client = ApiConfig.getApiService().register(name, email, password)
//        client.enqueue(object : Callback<PostResponseRegister> {
//            override fun onResponse(
//                call: Call<PostResponseRegister>,
//                response: retrofit2.Response<PostResponseRegister>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//
//                    _errorMessage.value = response.body()?.message as String
//                } else {
//                    Log.e("MainActivity", "onFailure: ${response.message()}")
//
//                    _errorMessage.value = "Email Already Taken"
//                    _isError.value = true
//                }
//            }
//            override fun onFailure(call: Call<PostResponseRegister>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                _errorMessage.value = t.message
//                Log.e("MainActivity", "onFailure: ${t.message}")
//            }
//        })
    }
}