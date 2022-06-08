package com.android.project.ecotrans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
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
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViewModel()
        setupView()
        setupAction()
//        setupAnimation()
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[ProfileViewModel::class.java]

        profileViewModel.errorMessage.observe(this){
            showErrorMessage(it)
        }

        profileViewModel.isLoadingProfileData.observe(this) {
            showLoading(it)
        }

        profileViewModel.isDetailed.observe(this){
            if(it){
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

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
        binding.autoCompleteTextViewProfileVourcherInterest.setAdapter(ArrayAdapter(this, R.layout.dropdown_profile_item, resources.getStringArray(R.array.interest)))
        binding.autoCompleteTextViewProfileEducation.setAdapter(ArrayAdapter(this, R.layout.dropdown_profile_item, resources.getStringArray(R.array.education)))
        binding.autoCompleteTextViewProfileMarriageStatus.setAdapter(ArrayAdapter(this, R.layout.dropdown_profile_item, resources.getStringArray(R.array.status)))
        binding.autoCompleteTextViewProfileVehicle.setAdapter(ArrayAdapter(this, R.layout.dropdown_profile_item, resources.getStringArray(R.array.vehicle)))
        binding.autoCompleteTextViewProfileGender.setAdapter(ArrayAdapter(this, R.layout.dropdown_profile_item, resources.getStringArray(R.array.gender)))

    }

    private fun setupAction() {
        binding.btnSave.setOnClickListener {

            val isMarried: Boolean = binding.autoCompleteTextViewProfileMarriageStatus.text.toString() == "True"
            val income = binding.autoCompleteTextViewProfileIncome.text.toString().toInt()

            val json = JSONObject()
            json.put("job", binding.autoCompleteTextViewProfileJob.text)
            json.put("voucherInterest", binding.autoCompleteTextViewProfileVourcherInterest.text)
            json.put("domicile", binding.autoCompleteTextViewProfileDomicile.text)
            json.put("education", binding.autoCompleteTextViewProfileEducation.text)
            json.put("marriageStatus", isMarried)
            json.put("income", income)
            json.put("vehicle", binding.autoCompleteTextViewProfileVehicle.text)
            json.put("gender", binding.autoCompleteTextViewProfileGender.text)
            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            if (!token.isNullOrEmpty() && !id.isNullOrEmpty()){
                profileViewModel.addProfileData(token, id, requestBody)
            }
        }

        binding.imageViewProfileBack.setOnClickListener {
            finish()
        }
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarSave.visibility = View.VISIBLE
            binding.btnSave.visibility = View.GONE
        } else {
            binding.progressBarSave.visibility = View.GONE
            binding.btnSave.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@ProfileActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}