package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {

    var input: String = "jalan"
    fun querySearch(string: String){
        input = string
//        searchUser()
    }

    private var _listPredictionsItem = MutableLiveData<List<PredictionsItem>>()
    var listPredictionsItem: LiveData<List<PredictionsItem>> = _listPredictionsItem

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    init {
    }

    fun getUser(id: String) {
        _isLoading.value = true
        _isError.value = false
        var user: User
        var client = ApiConfig.getApiService().user(id)
        client.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: retrofit2.Response<User>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    user = response.body() as User
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

    fun searchLocation(input: String) {
        _isLoading.value = true
        _isError.value = false
        var autoLocation: ResponseAutoComplete

        val json = JSONObject()
        json.put("input", input)
        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        var client = ApiConfig.getApiService().searchLocation(requestBody)
        client.enqueue(object : Callback<ResponseAutoComplete> {
            override fun onResponse(
                call: Call<ResponseAutoComplete>,
                response: retrofit2.Response<ResponseAutoComplete>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {

                    _listPredictionsItem.value = response.body()?.predictions as List<PredictionsItem>

//                    _errorMessage.value = "login " + response.message() as String
                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

//                    _errorMessage.value = "Wrong Password or Email"
//                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseAutoComplete>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

}