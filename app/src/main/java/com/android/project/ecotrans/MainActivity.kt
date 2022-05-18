package com.android.project.ecotrans

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.android.project.ecotrans.databinding.ActivityMainBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.view_model.LoginViewModel
import com.android.project.ecotrans.view_model.MainViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

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
            Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }
}