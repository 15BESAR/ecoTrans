package com.android.project.ecotrans

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.RecyclerView
import com.android.project.ecotrans.databinding.ActivityPurchaseVoucherlistItemBinding
import com.android.project.ecotrans.response.ResponseVoucher
import com.android.project.ecotrans.view_model.PurchaseViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PurchaseVoucherListAdapter() : RecyclerView.Adapter<PurchaseVoucherListAdapter.PurchaseVoucherListHolder>(){
    private lateinit var context: Context

    fun setContext(context: Context){
        this.context = context
    }

    private val listVoucher: ArrayList<ResponseVoucher> = ArrayList()
    fun setListVoucher(listVoucher: List<ResponseVoucher>){
        this.listVoucher.addAll(listVoucher)
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
        holder.bind(listVoucher[position], purchaseViewModel)

    }

    override fun getItemCount(): Int {
        return listVoucher.size
    }

    class PurchaseVoucherListHolder(private val binding: ActivityPurchaseVoucherlistItemBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ResponseVoucher, purchaseViewModel: PurchaseViewModel) {
            with(binding) {
//                Glide.with(itemView.context)
//                    .load(user.avatar_url)
//                    .circleCrop()
//                    .into(imgAvatar)
                textViewVoucherListItemName.text = data.voucherName
                textViewVoucherListItemCategory.text = data.category
                textViewVoucherListItemPartner.text = data.partnerName
                textViewVoucherListItemPrice.text = data.price.toString()
                textViewVoucherListItemStock.text = data.stock.toString()

                binding.imageButtonVoucherListItemBuy.setOnClickListener {
                    purchaseViewModel.test(data.voucherName.toString())
                }
            }
        }
    }
}