package com.android.project.ecotrans

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityMainBinding
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.view_model.LoginViewModel
import com.android.project.ecotrans.view_model.MainViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var token: String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.main_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "insert..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.querySearch(query)
                mainViewModel.listPredictionsItem.observe(this@MainActivity) { items ->
                    setLocationList(items)
                }
                mainViewModel.isLoadingLocationList.observe(this@MainActivity) {
                    showLoadingLocationList(it)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.main_setting){
//            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }else if (item.itemId == R.id.main_profile){
//            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra("token").toString()

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
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[MainViewModel::class.java]

        mainViewModel.errorMessage.observe(this) {
            showErrorMessage(it)
        }

        //livedata Dashboard
        mainViewModel.getUser().observe(this){
            mainViewModel.getUserData(token, it.id)
        }
        mainViewModel.userData.observe(this){
            setDashboard(it)
        }
        mainViewModel.isLoadingDashboard.observe(this) {
            showLoadingMainDashboard(it)
        }
        mainViewModel.getUser().observe(this) {
            token = it.token
        }

        //livedata LocationList
        mainViewModel.listPredictionsItem.observe(this) { items ->
            setLocationList(items)
        }
        mainViewModel.isLoadingLocationList.observe(this) {
            showLoadingLocationList(it)
        }

        //!isLogin
        mainViewModel.getUser().observe(this) { user ->
            if(!user.isLogin){
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        //!isDetailed
        mainViewModel.isDetailed.observe(this){
            if (!it){
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        }

    }

    private fun setupView() {
        TODO("Not yet implemented")
    }

    private fun setDashboard(user: User) {
        user.firstName
        user.lastName
        user.points
        user.voucherInterest
        user.username
        user.email
    }

    private fun setLocationList(it: List<PredictionsItem>) {
        val adapter = MainActivityAdapter(it, this)
        binding.recyclerViewMainLocationList.adapter = adapter

        adapter.setOnItemClickCallback(object : MainActivityAdapter.OnItemClickback{
            override fun onItemClicked(location: PredictionsItem) {
                selectedLocation(location)
            }
        })
    }
    private fun selectedLocation(location: PredictionsItem){
        val intent = Intent(this@MainActivity, LocationDetailActivity::class.java)
        intent.putExtra("Location", location)
        startActivity(intent)
    }

    private fun showLoadingMainDashboard(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarMainDashboard.visibility = View.VISIBLE
        } else {
            binding.progressBarMainDashboard.visibility = View.GONE
        }
    }

    private fun showLoadingLocationList(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarMainLocationList.visibility = View.VISIBLE
        } else {
            binding.progressBarMainLocationList.visibility = View.GONE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@MainActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}