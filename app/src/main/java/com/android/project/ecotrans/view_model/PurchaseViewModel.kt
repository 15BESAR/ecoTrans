package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.ResponseGetAllVoucher
import com.android.project.ecotrans.response.ResponsePurchaseVoucher
import com.android.project.ecotrans.response.VouchersItem
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.util.ArrayList

class PurchaseViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private var _isError = MutableLiveData<Boolean>()
    var isError: LiveData<Boolean> = _isError

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private var _isBought = MutableLiveData<Boolean>()
    var isBought: LiveData<Boolean> = _isBought

    private var _vouchers = MutableLiveData<List<VouchersItem>>()
    var vouchers: LiveData<List<VouchersItem>> = _vouchers

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    init {
    }

    fun getAllVoucher(token: String) {
        _isLoading.value = true
        _isError.value = false
        var client = ApiConfig.getApiService().getAllVoucher("Bearer $token")
        client.enqueue(object : Callback<ResponseGetAllVoucher> {
            override fun onResponse(
                call: Call<ResponseGetAllVoucher>,
                response: retrofit2.Response<ResponseGetAllVoucher>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _vouchers.value = response.body()?.vouchers as List<VouchersItem>

                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

//                    _errorMessage.value = "Wrong Password or Email"
//                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponseGetAllVoucher>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
//                _errorMessage.value = t.message as String
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

    fun purchaseVoucher(token: String, requestBody: RequestBody, voucherName: String) {
        _isLoading.value = true
        _isError.value = false
        var client = ApiConfig.getApiService().purchase("Bearer $token", requestBody)
        client.enqueue(object : Callback<ResponsePurchaseVoucher> {
            override fun onResponse(
                call: Call<ResponsePurchaseVoucher>,
                response: retrofit2.Response<ResponsePurchaseVoucher>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _errorMessage.value = "$voucherName voucher bought"
                    _isBought.value = true

                } else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")

                    _errorMessage.value = "User points is not enough!"
//                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<ResponsePurchaseVoucher>, t: Throwable) {
//                _isLoading.value = false
//                _isError.value = true
                _errorMessage.value = "User points is not enough!"
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}