package com.android.project.ecotrans

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.RecyclerView
import com.android.project.ecotrans.databinding.ActivityPurchaseVoucherlistItemBinding
import com.android.project.ecotrans.response.VouchersItem
import com.android.project.ecotrans.view_model.PurchaseViewModel
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PurchaseVoucherListAdapter() : RecyclerView.Adapter<PurchaseVoucherListAdapter.PurchaseVoucherListHolder>(){
    private lateinit var context: Context

    fun setContext(context: Context){
        this.context = context
    }

    private val listVoucher: ArrayList<VouchersItem> = ArrayList()
    fun setListVoucher(listVoucher: List<VouchersItem>){
        this.listVoucher.addAll(listVoucher)
    }

    private lateinit var userId: String
    fun setUserId(userId: String){
        this.userId = userId
    }

    private lateinit var token: String
    fun setToken(token: String){
        this.token = token
    }

    private lateinit var purchaseViewModel: PurchaseViewModel
    fun setViewModel(purchaseViewModel: PurchaseViewModel){
        this.purchaseViewModel = purchaseViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseVoucherListHolder {
        val binding = ActivityPurchaseVoucherlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PurchaseVoucherListHolder(binding, context)
    }

    override fun onBindViewHolder(holder: PurchaseVoucherListHolder, position: Int) {
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(listVoucher[holder.adapterPosition])
//        }
        holder.bind(this.listVoucher[position], this.purchaseViewModel, this.userId, this.token)

    }

    override fun getItemCount(): Int {
        return listVoucher.size
    }

    class PurchaseVoucherListHolder(private val binding: ActivityPurchaseVoucherlistItemBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            data: VouchersItem,
            purchaseViewModel: PurchaseViewModel,
            userId: String,
            token: String
        ) {
            with(binding) {
//                Glide.with(itemView.context)
//                    .load(data.imageUrl)
//                    .circleCrop()
//                    .into(binding.imageViewVoucherListItemSmallMap)

                textViewVoucherListItemName.text = data.voucherName
                textViewVoucherListItemCategory.text = data.category
                textViewVoucherListItemPartner.text = data.partnerName
                textViewVoucherListItemPrice.text = data.price.toString()
                textViewVoucherListItemStock.text = data.stock.toString()

                binding.imageButtonVoucherListItemBuy.setOnClickListener {
                    val json = JSONObject()
                    json.put("userId", userId)
                    json.put("voucherId", data.voucherId)
                    json.put("buyDate", Calendar.getInstance().time)
                    json.put("buyQuantity", 1)
                    val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

                    purchaseViewModel.purchaseVoucher(token, requestBody, data.voucherName.toString())
                }
            }
        }
    }
}