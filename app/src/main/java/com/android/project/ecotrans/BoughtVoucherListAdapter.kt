package com.android.project.ecotrans

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.RecyclerView
import com.android.project.ecotrans.databinding.ActivityBoughtVoucherlistItemBinding
import com.android.project.ecotrans.response.Voucher
import com.android.project.ecotrans.response.VouchersItem

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class BoughtVoucherListAdapter() : RecyclerView.Adapter<BoughtVoucherListAdapter.BoughtVoucherListHolder>(){
    private lateinit var context: Context

    fun setContext(context: Context){
        this.context = context
    }

    private val listVoucher: ArrayList<Voucher> = ArrayList()
    fun setListVoucher(listVoucher: ArrayList<Voucher>){
        this.listVoucher.addAll(listVoucher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoughtVoucherListHolder {
        val binding = ActivityBoughtVoucherlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoughtVoucherListHolder(binding, context)
    }

    override fun onBindViewHolder(holder: BoughtVoucherListHolder, position: Int) {
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(listVoucher[holder.adapterPosition])
//        }
        holder.bind(listVoucher[position])

    }

    override fun getItemCount(): Int {
        return listVoucher.size
    }

    class BoughtVoucherListHolder(private val binding: ActivityBoughtVoucherlistItemBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Voucher) {
            with(binding) {
//                Glide.with(itemView.context)
//                    .load(user.avatar_url)
//                    .circleCrop()
//                    .into(imgAvatar)
                textViewVoucherListItemName.text = data.voucherName
                textViewVoucherListItemCategory.text = data.category
                textViewVoucherListItemPartner.text = data.partnerName
                textViewVoucherListItemPrice.text = data.price.toString()
            }
        }
    }
}