package com.android.project.ecotrans

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.project.ecotrans.databinding.ActivityPurchaseVoucherlistItemBinding
import com.android.project.ecotrans.model.UserPreference
import com.android.project.ecotrans.response.ResponseVoucher
import com.android.project.ecotrans.view_model.PurchaseViewModel
import com.android.project.ecotrans.view_model.ViewModelFactory

class PurchaseVoucherListAdapter() : RecyclerView.Adapter<PurchaseVoucherListAdapter.PurchaseVoucherListHolder>(){
    private lateinit var onItemClickCallback: OnItemClickback
    private lateinit var context: Context

    fun setContext(context: Context){
        this.context = context
    }

    private val listVoucher: ArrayList<ResponseVoucher> = ArrayList()
    fun setListVoucher(listVoucher: List<ResponseVoucher>){
        this.listVoucher.addAll(listVoucher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseVoucherListHolder {
        val binding = ActivityPurchaseVoucherlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PurchaseVoucherListHolder(binding, context)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: PurchaseVoucherListHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listVoucher[holder.adapterPosition])
        }
        holder.bind(listVoucher[position])
    }

    override fun getItemCount(): Int {
        return listVoucher.size
    }

    class PurchaseVoucherListHolder(private val binding: ActivityPurchaseVoucherlistItemBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ResponseVoucher) {
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
            }

        }
    }

    interface OnItemClickback {
        fun onItemClicked(data: ResponseVoucher)
    }
}