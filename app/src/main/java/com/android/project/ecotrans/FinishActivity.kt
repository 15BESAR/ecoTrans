package com.android.project.ecotrans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityFinishBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.ResponseFinish
import com.android.project.ecotrans.view_model.FinishViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.properties.Delegates

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding
    private lateinit var finishViewModel: FinishViewModel

    private lateinit var token: String
    private lateinit var id: String

    private lateinit var destinationId: String
    private lateinit var originId: String
    private var distance by Delegates.notNull<Int>()
    private var carbon by Delegates.notNull<Int>()
    private var reward by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.originId = intent.getStringExtra("originId") as String
        this.destinationId = intent.getStringExtra("destinationId") as String
        this.distance = intent.getIntExtra("distance", 0)
        this.carbon = intent.getIntExtra("carbon", 0)
        this.reward = intent.getIntExtra("reward", 0)

        setupViewModel()
        setupView()
        setupAction()
//        setupAnimation()
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }

    private fun setupViewModel() {
        finishViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[FinishViewModel::class.java]

        finishViewModel.isLoading.observe(this){
            showLoading(it)
        }

        finishViewModel.errorMessage.observe(this){
            showErrorMessage(it)
        }

        finishViewModel.getUser().observe(this){
            if (it != null){
                this.id = it.id
                this.token = it.token

                //finish journey
                val json = JSONObject()
                json.put("userId", "37778226-035e-417c-8baf-509e888657f2")
                json.put("origin", "ChIJl02Bz3GMaS4RCgefgFZdKtI")
                json.put("destination", "ChIJY9TrwiH0aS4RrvGqlZvI_Mw")
                json.put("startTime", "2018-12-10T13:49:51.141Z")
                json.put("finishTime", "2018-12-10T16:49:51.141Z")
                json.put("distanceTravelled", 10.43F)
                json.put("emissionSaved", 4.45F)
                json.put("reward", 102)
                val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

                finishViewModel.postJourney(this.token, requestBody)
            }
        }

        finishViewModel.finishData.observe(this){
            if(it != null){
                setupViewFinish(it)
            }
        }




    }

    private fun setupViewFinish(it: ResponseFinish) {
        binding.textViewFinishReward.text = it.reward.toString()
        binding.textViewFinishEmissionSaved.text = it.emissionSaved.toString()
        binding.textViewFinishDistanceTraveled.text = it.distanceTravelled.toString()
        binding.textViewFinishJourneyId.text = it.journeyId.toString()
        binding.textViewFinishTimeStart.text = it.finishTime.toString()
        binding.textViewFinishTimeFinish.text = it.startTime.toString()

    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnGoBackToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun showLoading(it: Boolean) {
        if (it) {
            binding.progressBarFinish.visibility = View.VISIBLE
            binding.textViewFinishReward.visibility = View.GONE
            binding.textViewFinishEmissionSaved.visibility = View.GONE
            binding.textViewFinishDistanceTraveled.visibility = View.GONE
            binding.textViewFinishJourneyId.visibility = View.GONE
            binding.textViewFinishTimeStart.visibility = View.GONE
            binding.textViewFinishTimeFinish.visibility = View.GONE
        } else {
            binding.progressBarFinish.visibility = View.GONE
            binding.textViewFinishReward.visibility = View.VISIBLE
            binding.textViewFinishEmissionSaved.visibility = View.VISIBLE
            binding.textViewFinishDistanceTraveled.visibility = View.VISIBLE
            binding.textViewFinishJourneyId.visibility = View.VISIBLE
            binding.textViewFinishTimeStart.visibility = View.VISIBLE
            binding.textViewFinishTimeFinish.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@FinishActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}