package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
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

    private fun searchLocation(){
        _isLoading.value = true
        _isError.value = false
        var client = ApiConfig.getApiService().searchLocation(input)
        client.enqueue(object : Callback<ResponseAutoComplete> {
            override fun onResponse(
                call: Call<ResponseAutoComplete>,
                response: Response<ResponseAutoComplete>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listPredictionsItem.value = response.body()?.predictions as List<PredictionsItem>
                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")
                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseAutoComplete>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}