package com.android.project.ecotrans

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
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
import com.android.project.ecotrans.view_model.MainViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory
import kotlin.properties.Delegates

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String
    private var points by Delegates.notNull<Int>()

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu, menu)
//
//        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
//
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = menu.findItem(R.id.main_search).actionView as SearchView
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        searchView.queryHint = "input location name..."
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//
//            override fun onQueryTextSubmit(query: String): Boolean {
////                mainViewModel.querySearch(query)
////                mainViewModel.listPredictionsItem.observe(this@MainActivity) { items ->
////                    setLocationList(items)
////                }
////                mainViewModel.isLoadingLocationList.observe(this@MainActivity) {
////                    showLoadingLocationList(it)
////                }
//                searchView.clearFocus()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                return false
//            }
//        })
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.main_setting){
////            val intent = Intent(this, ConfigActivity::class.java)
//            startActivity(intent)
//        }else if (item.itemId == R.id.main_profile){
////            val intent = Intent(this, FavoriteActivity::class.java)
////            startActivity(intent)
//
//            mainViewModel = ViewModelProvider(
//                this,
//                ViewModelFactory(UserPreference.getInstance(dataStore), this)
//            )[MainViewModel::class.java]
//            mainViewModel.logout()
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        setupAction()
//        setupAnimation()
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
            this.points = it.points as Int
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

    private fun setupView() {

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMainLocationList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewMainLocationList.addItemDecoration(itemDecoration)
    }

    private fun setupAction() {
        binding.btnPurchase.setOnClickListener{
            var isDetailed = !binding.textViewMainVoucherInterest.text.isNullOrEmpty()

            val intent = Intent(this, PurchaseActivity::class.java)
            intent.putExtra("isDetailed", isDetailed)
            intent.putExtra("points", points)
            startActivity(intent)
        }

        binding.btnBought.setOnClickListener {
            val intent = Intent(this, BoughtActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            mainViewModel.logout()
        }


        binding.imageViewMainSearch.setOnClickListener {
                    mainViewModel.querySearch(binding.editTextMainSearch.text.toString())
                    mainViewModel.listPredictionsItem.observe(this@MainActivity) { items ->
                        setupLocationList(items as ArrayList<PredictionsItem>)
                    }
                    mainViewModel.isLoadingLocationList.observe(this@MainActivity) {
                        showLoadingLocationList(it)
                    }

            binding.editTextMainSearch.text.clear()
        }

        binding.editTextMainSearch.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    mainViewModel.querySearch(binding.editTextMainSearch.text.toString())
                    mainViewModel.listPredictionsItem.observe(this@MainActivity) { items ->
                        setupLocationList(items as ArrayList<PredictionsItem>)
                    }
                    mainViewModel.isLoadingLocationList.observe(this@MainActivity) {
                        showLoadingLocationList(it)
                    }

                    binding.editTextMainSearch.text.clear()
                    return true
                }
                return false
            }
        })
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }

    private fun setupLocationList(items: ArrayList<PredictionsItem>){
        val listLocation = ArrayList<PredictionsItem>()
        var location : PredictionsItem
        for (item in items!!){
            location = PredictionsItem()

            location.placeId = item.placeId
            location.description = item.description
            location.types = item.types

            listLocation.add(location)
        }

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
        intent.putExtra("location", data.description)
        startActivity(intent)
    }

    private fun setDashboard(user: User) {
        binding.textViewMainFirstname.text = user.firstName
        binding.textViewMainLastname.text = user.lastName
        binding.textViewMainPoints.text = user.points.toString()
        binding.textViewMainVoucherInterest.text = user.voucherInterest
        binding.textViewMainUsername.text = user.username
        binding.textViewMainEmail.text = user.email
    }

    private fun showLoadingMainDashboard(isLoading: Boolean) {
        if (isLoading) {
            binding.textViewMainFirstname.visibility = View.GONE
            binding.textViewMainLastname.visibility = View.GONE
            binding.textViewMainEmail.visibility = View.GONE
            binding.btnPurchase.visibility = View.GONE
            binding.btnLogout.visibility = View.GONE
            binding.btnBought.visibility = View.GONE
            binding.textViewMainUsername.visibility = View.GONE
            binding.textViewMainVoucherInterest.visibility = View.GONE
            binding.textViewMainPoints.visibility = View.GONE
            binding.progressBarMainDashboard.visibility = View.VISIBLE
        } else {
            binding.textViewMainFirstname.visibility = View.VISIBLE
            binding.textViewMainLastname.visibility = View.VISIBLE
            binding.textViewMainEmail.visibility = View.VISIBLE
            binding.btnPurchase.visibility = View.VISIBLE
            binding.btnBought.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.VISIBLE
            binding.progressBarMainDashboard.visibility = View.GONE
            binding.textViewMainUsername.visibility = View.VISIBLE
            binding.textViewMainVoucherInterest.visibility = View.VISIBLE
            binding.textViewMainPoints.visibility = View.VISIBLE
        }
    }

    private fun showLoadingLocationList(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarMainLocationList.visibility = View.VISIBLE
            binding.recyclerViewMainLocationList.visibility = View.GONE
        } else {
            binding.progressBarMainLocationList.visibility = View.GONE
            binding.recyclerViewMainLocationList.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@MainActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}