package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

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

    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        _isError.value = false
        var userModel: UserModel
        var client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(
                call: Call<ResponseLogin>,
                response: retrofit2.Response<ResponseLogin>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    var id = response.body()?.loginResult?.userId as String
                    var name = response.body()?.loginResult?.name as String
                    var isLogin = true
                    var token = response.body()?.loginResult?.token as String
                    userModel = UserModel(id, name, isLogin, token)

                    _errorMessage.value = "login " + response.body()?.message as String
                    saveUser(userModel)
                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

                    _errorMessage.value = "Wrong Password or Email"
                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}