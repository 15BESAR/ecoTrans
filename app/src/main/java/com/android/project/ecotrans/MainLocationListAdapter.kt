package com.android.project.ecotrans

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.project.ecotrans.response.PredictionsItem


class MainActivityAdapter(private val listLocation: List<PredictionsItem>, private val context: Context) : RecyclerView.Adapter<MainActivityAdapter.MainActivityHolder>(){
    private lateinit var onItemClickCallback: OnItemClickback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_main_locationlist_item, parent, false)
        return MainActivityHolder(view)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: MainActivityHolder, position: Int) {
//        val (id, avatar_url, url, followers_url, following_url,
//            name, company, public_repos, follower,
//            following, type, login) = listUser[position]

//        Glide.with(holder.itemView.context)
//            .load(avatar_url)
//            .circleCrop()
//            .into(holder.imgAvatar)
//
//        holder.tvName.text = login
//        holder.tvType.text = type

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listLocation[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int {
        return listLocation.size
    }

    class MainActivityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgSmallMap: ImageView = itemView.findViewById(R.id.imageView_locationListItem_smallMap)
        var tvName: TextView = itemView.findViewById(R.id.textView_locationListItem_name)
        var tvDetail: TextView = itemView.findViewById(R.id.textView_locationListItem_detail)
    }

    interface OnItemClickback {
        fun onItemClicked(data: PredictionsItem)
    }
}