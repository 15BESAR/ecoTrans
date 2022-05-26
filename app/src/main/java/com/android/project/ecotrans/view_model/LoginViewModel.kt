package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
import com.android.project.ecotrans.response.ResponseLogin
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

//    var input: String = "jalan"
//    fun querySearch(string: String){
//        input = string
////        searchUser()
//    }
//
//    private var _listPredictionsItem = MutableLiveData<List<PredictionsItem>>()
//    var listPredictionsItem: LiveData<List<PredictionsItem>> = _listPredictionsItem

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

//    private var _userData = MutableLiveData<User>()
//    var userData: LiveData<User> = _userData



    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    init {
//        searchLocation(input)
    }

    fun postLogin(username: String, password: String) {
        _isLoading.value = true
        _isError.value = false
        var userModel: UserModel

        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())


        var client = ApiConfig.getApiService().login(requestBody)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(
                call: Call<ResponseLogin>,
                response: retrofit2.Response<ResponseLogin>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    var id = response.body()?.userId as String
                    var isLogin = true

                    var token = response.body()?.token as String
                    userModel = UserModel(id, username, isLogin, token)

                    _errorMessage.value = "login " + response.message() as String
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