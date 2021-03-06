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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.project.ecotrans.databinding.ActivityPurchaseBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.VouchersItem
import com.android.project.ecotrans.view_model.PurchaseViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PurchaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPurchaseBinding
    private lateinit var purchaseViewModel: PurchaseViewModel
    private lateinit var userId: String
    private lateinit var token: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isDetailed:Boolean = intent.getBooleanExtra("isDetailed", false)
        val points:Int = intent.getIntExtra("points", 0)

        setupViewModel(isDetailed)
        setupView(points)
        setupAction()
//        setupAnimation()
    }



    private fun setupViewModel(isDetailed: Boolean) {
        purchaseViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[PurchaseViewModel::class.java]

        purchaseViewModel.getUser().observe(this){
            if (!isDetailed){
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
        }

        purchaseViewModel.isLoading.observe(this){
            showLoading(it)
        }

        purchaseViewModel.errorMessage.observe(this){
            showErrorMessage(it)
        }

        purchaseViewModel.isBought.observe(this){
            if (it){
                startActivity(intent)
                finish()
            }
        }

        purchaseViewModel.getUser().observe(this){
            if(!it.id.isNullOrEmpty()){
                this.userId = it.id
                this.token = it.token
                purchaseViewModel.getAllVoucher(this.token)
            }
        }

        purchaseViewModel.vouchers.observe(this){
            if(it != null){
                setupVoucherList(it)
            }
        }
    }

    private fun setupView(points: Int) {
        supportActionBar?.hide()

        binding.textViewPurchasePointsValue.text = points.toString()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPurchaseVoucherlist.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewPurchaseVoucherlist.addItemDecoration(itemDecoration)

//        setupVoucherList()
    }

    private fun setupAction() {
        binding.imageViewPurchaseBack.setOnClickListener {
            finish()
        }
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }

    private fun setupVoucherList(list: List<VouchersItem>) {
//        val listVoucher = ArrayList<ResponseVoucher>()
//        var voucher : ResponseVoucher
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

//        voucher = ResponseVoucher()
//        voucher.category = "GoFood"
//        voucher.partnerName = "GOJEK"
//        voucher.price = 500
//        voucher.stock = 10
//        voucher.voucherName = "Diskon 50% hingga 25RB"
//        listVoucher.add(voucher)
//
//        voucher = ResponseVoucher()
//        voucher.category = "Furniture"
//        voucher.partnerName = "Tokopedia"
//        voucher.price = 1000
//        voucher.stock = 10
//        voucher.voucherName = "Diskon 5% hingga 100RB"
//        listVoucher.add(voucher)
//
//        voucher = ResponseVoucher()
//        voucher.category = "GoSend"
//        voucher.partnerName = "GOJEK"
//        voucher.price = 200
//        voucher.stock = 10
//        voucher.voucherName = "Gratis Ongkir hingga 10RB"
//        listVoucher.add(voucher)
//
//        voucher = ResponseVoucher()
//        voucher.category = "GoFood"
//        voucher.partnerName = "GOJEK"
//        voucher.price = 500
//        voucher.stock = 10
//        voucher.voucherName = "Diskon 50% hingga 25RB"
//        listVoucher.add(voucher)
//
//        voucher = ResponseVoucher()
//        voucher.category = "Furniture"
//        voucher.partnerName = "Tokopedia"
//        voucher.price = 1000
//        voucher.stock = 10
//        voucher.voucherName = "Diskon 5% hingga 100RB"
//        listVoucher.add(voucher)
//
//        voucher = ResponseVoucher()
//        voucher.category = "GoSend"
//        voucher.partnerName = "GOJEK"
//        voucher.price = 200
//        voucher.stock = 10
//        voucher.voucherName = "Gratis Ongkir hingga 10RB"
//        listVoucher.add(voucher)

        val adapter = PurchaseVoucherListAdapter()
        adapter.setContext(this)
        adapter.setListVoucher(list)
        adapter.setViewModel(purchaseViewModel)
        adapter.setUserId(this.userId)
        adapter.setToken(this.token)
        binding.recyclerViewPurchaseVoucherlist.adapter = adapter

//        adapter.setOnItemClickCallback(object : PurchaseVoucherListAdapter.OnItemClickback{
//            override fun onItemClicked(data: ResponseVoucher) {
//                buyVoucher(data)
//            }
//        })
    }

//    private fun buyVoucher(data: ResponseVoucher) {
//        //
//    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarPurchaseVoucherlist.visibility = View.VISIBLE
            binding.recyclerViewPurchaseVoucherlist.visibility = View.GONE
        } else {
            binding.progressBarPurchaseVoucherlist.visibility = View.GONE
            binding.recyclerViewPurchaseVoucherlist.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@PurchaseActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}