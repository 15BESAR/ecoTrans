package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class BoughtViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _boughtVouchers = MutableLiveData<ArrayList<Voucher>>()
    var boughtVoucher: LiveData<ArrayList<Voucher>> = _boughtVouchers

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    init {
    }

    fun getBoughtVoucher(token: String, id:String) {
        _isLoading.value = true
        _isError.value = false
        var client = ApiConfig.getApiService().getBoughtVoucher("Bearer $token", id)
        client.enqueue(object : Callback<ResponseGetAllPurchaseHistory> {
            override fun onResponse(
                call: Call<ResponseGetAllPurchaseHistory>,
                response: retrofit2.Response<ResponseGetAllPurchaseHistory>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {

                    try {
                        val purchases = response.body()?.purchases as ArrayList<PurchasesItem>
                        for (item in purchases){
                            getVoucherById(token, item.voucherId.toString())
                        }
                    }catch (e: Exception){
                        _errorMessage.value = "Empty..."
                    }

                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

//                    _errorMessage.value = "Wrong Password or Email"
//                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseGetAllPurchaseHistory>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

    fun getVoucherById(token: String, voucherId:String) {
        _isLoading.value = true
        _isError.value = false
        var client = ApiConfig.getApiService().getVoucherById("Bearer $token", voucherId)
        client.enqueue(object : Callback<ResponseGetVoucherById> {
            override fun onResponse(
                call: Call<ResponseGetVoucherById>,
                response: retrofit2.Response<ResponseGetVoucherById>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _boughtVouchers.value?.add(response.body()?.voucher as Voucher)

                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

//                    _errorMessage.value = "Wrong Password or Email"
//                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseGetVoucherById>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}