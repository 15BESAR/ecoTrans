package com.android.project.ecotrans

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityProfileBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.ProfileViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var token: String
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupView()
        setupViewModel()
        setupAction()
//        setupAnimation()
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }
//
    private fun setupAction() {
        binding.btnSave.setOnClickListener {
            val json = JSONObject()
//                json.put("username", username)
//                json.put("password", password)
            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            if (!token.isNullOrEmpty() && !id.isNullOrEmpty()){
                profileViewModel.addProfileData(token, id, requestBody)
            }
        }
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[ProfileViewModel::class.java]

        profileViewModel.getUser().observe(this){
            if (!it.token.isNullOrEmpty()){
                token = it.token
            }
            if (!it.id.isNullOrEmpty()){
                id = it.id
            }
        }


    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
//            binding.mprogressBar.visibility = View.VISIBLE
        } else {
//            binding.mprogressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean){
        if (isError){
            Toast.makeText(this@ProfileActivity, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }
}