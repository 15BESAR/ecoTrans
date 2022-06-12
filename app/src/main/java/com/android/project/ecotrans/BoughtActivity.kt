package com.android.project.ecotrans

import android.content.Context
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
import com.android.project.ecotrans.databinding.ActivityBoughtBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.PurchasesItem
import com.android.project.ecotrans.response.Voucher
import com.android.project.ecotrans.response.VouchersItem
import com.android.project.ecotrans.view_model.BoughtViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class BoughtActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoughtBinding
    private lateinit var boughtViewModel: BoughtViewModel

    private lateinit var userId: String
    private lateinit var token: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoughtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isDetailed:Boolean = intent.getBooleanExtra("isDetailed", false)

        setupViewModel(isDetailed)
        setupView()
        setupAction()
//        setupAnimation()
    }

    private fun setupViewModel(isDetailed: Boolean) {
        boughtViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[BoughtViewModel::class.java]

        boughtViewModel.errorMessage.observe(this){
            showErrorMessage(it)
        }

        boughtViewModel.getUser().observe(this){
            if (it != null){
                this.token = it.token
                this.userId = it.id
                boughtViewModel.getBoughtVoucher(this.token, this.userId)
            }
        }

        boughtViewModel.boughtVouchers.observe(this){
            if (it != null){
                setupVoucherList(it)
            }
        }

        boughtViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun setupView() {
        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewBoughtVoucherlist.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewBoughtVoucherlist.addItemDecoration(itemDecoration)
//        setupVoucherList()
    }

    private fun setupAction() {
        binding.imageViewBoughtBack.setOnClickListener {
            finish()
        }
    }

//    private fun setupAnimation() {
//        TODO("Not yet implemented")
//    }
//
//    private fun setupAction() {
//        TODO("Not yet implemented")
//    }

    private fun setupVoucherList(listBoughtVoucher: ArrayList<PurchasesItem>){
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

//        var voucher : ResponseVoucher = ResponseVoucher()
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

        var listBoughtVoucherById: ArrayList<PurchasesItem> = ArrayList()

        for (item in listBoughtVoucher){
            if (item.userId == this.userId){
                listBoughtVoucherById.add(item)
            }
        }

        val adapter = BoughtVoucherListAdapter()
        adapter.setContext(this)
        adapter.setListVoucher(listBoughtVoucherById)
        binding.recyclerViewBoughtVoucherlist.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarBoughtVoucherlist.visibility = View.VISIBLE
            binding.recyclerViewBoughtVoucherlist.visibility = View.GONE
        } else {
            binding.progressBarBoughtVoucherlist.visibility = View.GONE
            binding.recyclerViewBoughtVoucherlist.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(this@BoughtActivity, errorMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}