package com.example.newsapplication.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.DataClass.Data1
import com.example.newsapplication.R
import com.squareup.picasso.Picasso

class ItemsAdapter(val context:Context,
                   var listArr: MutableList<Data1.Article>,
                   private val onItemClick: (Data1.Article) -> Unit,
                   private val listener: OnFavoriteSelectedListener
):RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    fun updateArticles(newArticles: MutableList<Data1.Article>) {
        listArr.clear() // Clear existing data
        listArr.addAll(newArticles) // Add new data
        notifyDataSetChanged() // Notify the adapter of data change
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val AuthorName :TextView=itemView.findViewById(R.id.author)
        val TitleName:TextView=itemView.findViewById(R.id.Title)
        val PublishedAt:TextView=itemView.findViewById(R.id.publishedAt)
        val ImageToUrl:ImageView=itemView.findViewById(R.id.urlToImage)
        val favoriteNews: ConstraintLayout = itemView.findViewById(R.id.favouriteNews)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(listArr[position])
                }
            }
            favoriteNews.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onFavoriteSelected(listArr[position])
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.news_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listArr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listArr[position]
        Log.d("categoryAdapter", "Item at position $position: $currentItem")

        holder.AuthorName.text = currentItem.author
        holder.TitleName.text = currentItem.title
        holder.PublishedAt.text = currentItem.publishedAt
        Picasso.get().load(currentItem.urlToImage).into(holder.ImageToUrl)
    }
    interface OnFavoriteSelectedListener {
        fun onFavoriteSelected(article: Data1.Article)
    }


}