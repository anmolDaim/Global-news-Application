package com.example.newsapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.Adapter.ItemsAdapter
import com.example.newsapplication.Adapter.savedAdapter
import com.example.newsapplication.DataClass.Data1
import com.example.newsapplication.DataClass.SavedDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso


class SavedFragment : Fragment() {
    private lateinit var savedArticlesRecyclerView: RecyclerView
    lateinit var favText:TextView
    lateinit var favImage:ImageView

    fun setData(bundle: Bundle) {
        arguments = bundle
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)
        savedArticlesRecyclerView = view.findViewById(R.id.savedArticlesRecyclerView)
        favImage=view.findViewById(R.id.favImage)
        favText=view.findViewById(R.id.favText)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gson = Gson()
        val sharedPreferences = requireContext().getSharedPreferences("article_data", Context.MODE_PRIVATE)

// Retrieve JSON string from SharedPreferences
        val articleListJson = sharedPreferences.getString("articleList", null)

// Convert JSON string back to list
        val type = object : TypeToken<List<Data1.Article>>() {}.type
        val articleList: List<Data1.Article> = gson.fromJson(articleListJson, type)

        val arrSaved=ArrayList<SavedDataClass>()
       // if (author != null && title != null && publishedAt != null && urlToImage != null) {
         //   arrSaved.add(SavedDataClass(author, title, publishedAt, urlToImage))
        //}
        articleList.forEach { article ->
            val savedData = SavedDataClass(
                article.author ?: "",
                article.title ?: "",
                article.publishedAt ?: "",
                article.urlToImage ?: ""
            )
            arrSaved.add(savedData)
        }
        val linearLayout=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        savedArticlesRecyclerView.layoutManager = linearLayout
        val adapter = savedAdapter(arrSaved)
        savedArticlesRecyclerView.adapter = adapter

        if (arrSaved.isEmpty()) {
            savedArticlesRecyclerView.visibility = View.GONE
            favText.visibility = View.VISIBLE
            favImage.visibility = View.VISIBLE
        } else {
            savedArticlesRecyclerView.visibility = View.VISIBLE
            favText.visibility = View.GONE
            favImage.visibility = View.GONE
        }

    }


}