package com.android.project.ecotrans

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.project.ecotrans.databinding.ActivityMainLocationlistItemBinding
import com.android.project.ecotrans.response.PredictionsItem
import com.android.project.ecotrans.response.ResponseAutoComplete
import com.android.project.ecotrans.response.ResponseVoucher
import com.bumptech.glide.Glide

class MainLocationListAdapter() : RecyclerView.Adapter<MainLocationListAdapter.MainLocationListHolder>(){
    private lateinit var onItemClickCallback: OnItemClickback
    private lateinit var context: Context

    fun setContext(context: Context){
        this.context = context
    }

    private val listLocation: ArrayList<PredictionsItem> = ArrayList()
    fun setListLocation(listLocation: List<PredictionsItem>){
        this.listLocation.addAll(listLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainLocationListHolder {
        val binding = ActivityMainLocationlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainLocationListHolder(binding, context)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: MainLocationListHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listLocation[holder.adapterPosition])
        }
        holder.bind(listLocation[position])
    }

    override fun getItemCount(): Int {
        return listLocation.size
    }

    class MainLocationListHolder(private val binding: ActivityMainLocationlistItemBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PredictionsItem) {
            with(binding) {
//                Glide.with(itemView.context)
//                    .load(user.avatar_url)
//                    .circleCrop()
//                    .into(imgAvatar)

                textViewLocationListItemName.text = data.description
                textViewLocationListItemDetail.text = data.types?.get(0) ?: "none"
            }

        }
    }

    interface OnItemClickback {
        fun onItemClicked(data: PredictionsItem)
    }
}