package com.android.project.ecotrans

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityMainBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.view_model.MainViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
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
//                mainViewModel.listUser.observe(this@MainActivity) { items ->
//                    setItems(items)
//                }
//                mainViewModel.isLoading.observe(this@MainActivity) {
//                    showLoading(it)
//                }
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

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.errorMessage.observe(this) {
            showErrorMessage(it)
        }
        mainViewModel.getUser().observe(this) {
            token = it.token
        }
        mainViewModel.listPredictionsItem.observe(this) {
            setLocationList(it)
        }
        mainViewModel.getUser().observe(this) { user ->
            if(!user.isLogin){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }

    private fun setLocationList(it: List<PredictionsItem>) {
        val adapter = MainActivityAdapter(it, this)
        binding.recyclerViewMainLocationList.adapter = adapter

        adapter.setOnItemClickCallback(object : MainActivityAdapter.OnItemClickback{
            override fun onItemClicked(data: PredictionsItem) {
                //TODO
            }
        })
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

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@MainActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}