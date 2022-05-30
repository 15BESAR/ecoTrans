package com.android.project.ecotrans.view_model

import androidx.lifecycle.*
import com.android.project.ecotrans.model.UserModel
import com.android.project.ecotrans.model.UserPreference
import kotlinx.coroutines.launch

class SplashViewModel(private val pref: UserPreference) : ViewModel() {

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
}