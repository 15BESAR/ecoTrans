package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
import com.android.project.ecotrans.response.ResponseGetUser
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {

//    var input: String = "jalan"
//    fun querySearch(string: String){
//        input = string
//        searchLocation()
//    }

//    private var _listPredictionsItem = MutableLiveData<List<PredictionsItem>>()
//    var listPredictionsItem: LiveData<List<PredictionsItem>> = _listPredictionsItem
//    private var _isLoadingLocationList = MutableLiveData<Boolean>()
//    var isLoadingLocationList: LiveData<Boolean> = _isLoadingLocationList


    private var _userData = MutableLiveData<User>()
    var userData: LiveData<User> = _userData
    private var _isLoadingDasboard = MutableLiveData<Boolean>()
    var isLoadingDashboard: LiveData<Boolean> = _isLoadingDasboard


    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError
    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage


    private var _isDetailed = MutableLiveData<Boolean>()
    var isDetailed: LiveData<Boolean> = _isDetailed


    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    init {
    }

    fun getUserData(token: String, id: String) {
        _isLoadingDasboard.value = true
        _isError.value = false
        _isDetailed.value = true

        var client = ApiConfig.getApiService().user("Bearer $token", id)
        client.enqueue(object : Callback<ResponseGetUser> {
            override fun onResponse(
                call: Call<ResponseGetUser>,
                response: retrofit2.Response<ResponseGetUser>
            ) {
                _isLoadingDasboard.value = false
                if (response.isSuccessful) {
                    _userData.value = response.body()?.user as User
                    if(_userData.value?.job.isNullOrEmpty()){
                        _isDetailed.value = false
                    }

                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

//                    _errorMessage.value = "Wrong Password or Email"
//                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseGetUser>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

//    fun searchLocation() {
//
//        _isLoadingLocationList.value = true
//
//        _isError.value = false
//        var autoLocation: ResponseAutoComplete
//
//        val json = JSONObject()
//        json.put("input", input)
//        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
//
//        var client = ApiConfig.getApiService().searchLocation(requestBody)
//        client.enqueue(object : Callback<ResponseAutoComplete> {
//            override fun onResponse(
//                call: Call<ResponseAutoComplete>,
//                response: retrofit2.Response<ResponseAutoComplete>
//            ) {
//                _isLoadingLocationList.value = false
//                if (response.isSuccessful) {
//
//                    _listPredictionsItem.value = response.body()?.predictions as List<PredictionsItem>
//
////                    _errorMessage.value = "login " + response.message() as String
//                } else {
//                    Log.e("MainActivity", "onFailure: ${response.message()}")
//
////                    _errorMessage.value = "Wrong Password or Email"
////                    _isError.value = true
//                }
//            }
//            override fun onFailure(call: Call<ResponseAutoComplete>, t: Throwable) {
////                _isLoading.value = false
////                _isError.value = true
////                _errorMessage.value = t.message as String
//                Log.e("MainActivity", "onFailure: ${t.message}")
//            }
//        })
//    }

}