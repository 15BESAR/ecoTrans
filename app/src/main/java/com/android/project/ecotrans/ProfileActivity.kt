package com.android.project.ecotrans

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityMainBinding
import com.android.project.ecotrans.databinding.ActivityProfileBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.MainViewModel
import com.android.project.ecotrans.view_model.ProfileViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupView()
        setupViewModel()
        setupAction()
        setupAnimation()
    }

    private fun setupAnimation() {
        TODO("Not yet implemented")
    }

    private fun setupAction() {
        TODO("Not yet implemented")
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[ProfileViewModel::class.java]
    }

    private fun setupView() {
        TODO("Not yet implemented")
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