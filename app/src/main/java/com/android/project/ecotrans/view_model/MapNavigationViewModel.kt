package com.android.project.ecotrans.view_model

import android.util.Log
import androidx.lifecycle.*
import com.android.project.ecotrans.api_config.ApiConfig
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapNavigationViewModel(private val pref: UserPreference) : ViewModel() {

    private var _isLoadingPreferenceList = MutableLiveData<Boolean>()
    var isLoadingPreferenceList: LiveData<Boolean> = _isLoadingPreferenceList

    private var _isLoadingJourneyInformation = MutableLiveData<Boolean>()
    var isLoadingJourneyInformation: LiveData<Boolean> = _isLoadingJourneyInformation

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

//    fun getUserData(token: String, id: String) {
//        _isLoadingDasboard.value = true
//        _isError.value = false
//        _isDetailed.value = true
//
//        var client = ApiConfig.getApiService().user("Bearer $token", id)
//        client.enqueue(object : Callback<User> {
//            override fun onResponse(
//                call: Call<User>,
//                response: retrofit2.Response<User>
//            ) {
//                _isLoadingDasboard.value = false
//                if (response.isSuccessful) {
//                    _userData.value = response.body() as User
//                    if(_userData.value?.job.isNullOrEmpty()){
//                        _isDetailed.value = false
//                    }
//
//                } else {
//                    Log.e("MainActivity", "onFailure: ${response.message()}")
//
////                    _errorMessage.value = "Wrong Password or Email"
////                    _isError.value = true
//                }
//            }
//            override fun onFailure(call: Call<User>, t: Throwable) {
////                _isLoading.value = false
////                _isError.value = true
////                _errorMessage.value = t.message as String
//                Log.e("MainActivity", "onFailure: ${t.message}")
//            }
//        })
//    }
}