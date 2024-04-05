package com.example.newsapplication.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.DataClass.Data1
import com.example.newsapplication.DataClass.SavedDataClass
import com.example.newsapplication.R
import com.squareup.picasso.Picasso

class savedAdapter(val savedArr:ArrayList<SavedDataClass>):RecyclerView.Adapter<savedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val AuthorName : TextView =itemView.findViewById(R.id.savedauthor)
        val TitleName: TextView =itemView.findViewById(R.id.savedTitle)
        val PublishedAt: TextView =itemView.findViewById(R.id.savedpublishedAt)
        val ImageToUrl: ImageView =itemView.findViewById(R.id.savedurlToImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.saved_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedArr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = savedArr[position]
        Log.d("categoryAdapter", "Item at position $position: $currentItem")

        holder.AuthorName.text = currentItem.authorSaved
        holder.TitleName.text = currentItem.titleSaved
        holder.PublishedAt.text = currentItem.publishSaved
        Picasso.get().load(currentItem.imageSaved).into(holder.ImageToUrl)
    }
}