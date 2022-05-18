package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    var queries: String = "sidiqpermana"
    fun querySearch(string: String){
        queries = string
//        searchUser()
    }

//    private var _listUser = MutableLiveData<List<ItemsItem>>()
//    var listUser: LiveData<List<ItemsItem>> = _listUser

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError

    init {
    }

//    private fun searchUser(){
//        _isLoading.value = true
//        _isError.value = false
//        var client = ApiConfig.getApiService().searchUser(queries)
//        client.enqueue(object : Callback<ResponseQ> {
//            override fun onResponse(
//                call: Call<ResponseQ>,
//                response: Response<ResponseQ>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _listUser.value = response.body()?.items as List<ItemsItem>?
//                } else {
//                    Log.e("MainActivity", "onFailure: ${response.message()}")
//                    _isError.value = true
//                }
//            }
//            override fun onFailure(call: Call<ResponseQ>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                Log.e("MainActivity", "onFailure: ${t.message}")
//            }
//        })
//    }
}