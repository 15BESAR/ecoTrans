package com.android.project.ecotrans.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.FinishActivity
import com.android.project.ecotrans.model.UserPreference


class ViewModelFactory(private val pref: UserPreference, private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(PurchaseViewModel::class.java) -> {
                PurchaseViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LocationDetailViewModel::class.java) -> {
                LocationDetailViewModel(pref) as T
            }
            modelClass.isAssignableFrom(FinishViewModel::class.java) -> {
                FinishViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}

//class ViewMainModelFactory(private val pref: UserPreference, private val context: Context, private val token: String) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
////            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
////                MainViewModel(pref, Injection.provideRepository(context), token) as T
////            }
//            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//        }
//    }
//}