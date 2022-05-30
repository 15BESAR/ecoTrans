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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.project.ecotrans.databinding.ActivityMainBinding
import com.android.project.ecotrans.model.User
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
import com.android.project.ecotrans.response.ResponseVoucher
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
        searchView.queryHint = "input location name..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
//                mainViewModel.querySearch(query)
//                mainViewModel.listPredictionsItem.observe(this@MainActivity) { items ->
//                    setLocationList(items)
//                }
//                mainViewModel.isLoadingLocationList.observe(this@MainActivity) {
//                    showLoadingLocationList(it)
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
//            startActivity(intent)

            mainViewModel = ViewModelProvider(
                this,
                ViewModelFactory(UserPreference.getInstance(dataStore), this)
            )[MainViewModel::class.java]
            mainViewModel.logout()
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
//        setupAnimation()
    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMainLocationList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewMainLocationList.addItemDecoration(itemDecoration)
        setupLocationList()
    }

    private fun setupLocationList(){
        val listLocation = ArrayList<PredictionsItem>()
        var location : PredictionsItem
//        for (item in items!!){
//            user = User()
//
//            user.id = item?.id
//            user.login = item?.login
//            user.avatar_url = item?.avatarUrl
//            user.followers_url = item?.followersUrl
//            user.following_url = item?.followingUrl
//            user.type = item?.type
//            user.url = item?.url
//
//            listItem.add(user)
//        }

        location = PredictionsItem()
        location.description = "Jalan A"
        location.placeId = "id = 0"
        listLocation.add(location)

        location = PredictionsItem()
        location.description = "Jalan B"
        location.placeId = "id = 1"
        listLocation.add(location)

        location = PredictionsItem()
        location.description = "Jalan C"
        location.placeId = "id = 2"
        listLocation.add(location)

        location = PredictionsItem()
        location.description = "Jalan D"
        location.placeId = "id = 3"
        listLocation.add(location)

        location = PredictionsItem()
        location.description = "Jalan E"
        location.placeId = "id = 4"
        listLocation.add(location)

        location = PredictionsItem()
        location.description = "Jalan F"
        location.placeId = "id = 5"
        listLocation.add(location)


        val adapter = MainLocationListAdapter()
        adapter.setContext(this)
        adapter.setListLocation(listLocation)
        binding.recyclerViewMainLocationList.adapter = adapter

        adapter.setOnItemClickCallback(object : MainLocationListAdapter.OnItemClickback{
            override fun onItemClicked(data: PredictionsItem) {
                selectedLocationDetail(data)
            }
        })
    }

    private fun selectedLocationDetail(data: PredictionsItem) {
        intent = Intent(this, LocationDetailActivity::class.java)
        intent.putExtra("location", data)
        startActivity(intent)

    }

    //    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }
//
    private fun setupAction() {
        binding.btnPurchase.setOnClickListener{
            var isDetailed = !binding.textViewMainVoucherInterest.text.isNullOrEmpty()

            val intent = Intent(this, PurchaseActivity::class.java)
            intent.putExtra("isDetailed", isDetailed)
            startActivity(intent)
        }

//        binding.btnGotodetail.setOnClickListener {
//            val description = "Jalan Tubagus Ismail Raya No.76, Sekeloa, Kota Bandung, Jawa Barat, Indonesia"
//            val place_id = "ChIJTTd6FwDnaC4RkQxw5RdbZhk"
//            intent = Intent(this, LocationDetailActivity::class.java)
//            intent.putExtra("description", description)
//            intent.putExtra("place_id", place_id)
//        }
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
            if (!it.token.isNullOrEmpty()){
                token = it.token
                mainViewModel.getUserData(it.token, it.id)
            }

        }
        mainViewModel.userData.observe(this){
            setDashboard(it)
        }
        mainViewModel.isLoadingDashboard.observe(this) {
            showLoadingMainDashboard(it)
        }


//        //livedata LocationList
//        mainViewModel.listPredictionsItem.observe(this) { items ->
//            setLocationList(items)
//        }
//        mainViewModel.isLoadingLocationList.observe(this) {
//            showLoadingLocationList(it)
//        }

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

//    private fun setupView() {
//        TODO("Not yet implemented")
//    }

    private fun setDashboard(user: User) {
        binding.textViewMainFirstname.text = user.firstName
        binding.textViewMainLastname.text = user.lastName
        binding.textViewMainPoints.text = user.points.toString()
        binding.textViewMainVoucherInterest.text = user.voucherInterest
        binding.textViewMainUsername.text = user.username
        binding.textViewMainEmail.text = user.email
    }

//    private fun setLocationList(it: List<PredictionsItem>) {
//        val adapter = MainActivityAdapter(it, this)
//        binding.recyclerViewMainLocationList.adapter = adapter
//
//        adapter.setOnItemClickCallback(object : MainActivityAdapter.OnItemClickback{
//            override fun onItemClicked(location: PredictionsItem) {
//                selectedLocation(location)
//            }
//        })
//    }

//    private fun selectedLocation(location: PredictionsItem){
//        val intent = Intent(this@MainActivity, LocationDetailActivity::class.java)
//        intent.putExtra("Location", location)
//        startActivity(intent)
//    }

    private fun showLoadingMainDashboard(isLoading: Boolean) {
        if (isLoading) {
            binding.textViewMainFirstname.visibility = View.GONE
            binding.textViewMainLastname.visibility = View.GONE
            binding.textViewMainEmail.visibility = View.GONE
            binding.btnPurchase.visibility = View.GONE
            binding.progressBarMainDashboard.visibility = View.VISIBLE
        } else {
            binding.textViewMainFirstname.visibility = View.VISIBLE
            binding.textViewMainLastname.visibility = View.VISIBLE
            binding.textViewMainEmail.visibility = View.VISIBLE
            binding.btnPurchase.visibility = View.VISIBLE
            binding.progressBarMainDashboard.visibility = View.GONE
        }
    }

//    private fun showLoadingLocationList(isLoading: Boolean) {
//        if (isLoading) {
//            binding.progressBarMainLocationList.visibility = View.VISIBLE
//        } else {
//            binding.progressBarMainLocationList.visibility = View.GONE
//        }
//    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@MainActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}