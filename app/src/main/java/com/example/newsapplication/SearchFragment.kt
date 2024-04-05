package com.example.newsapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.Adapter.ItemsAdapter
import com.example.newsapplication.DataClass.Data1
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchFragment : Fragment(), ItemsAdapter.OnFavoriteSelectedListener {
    lateinit var backBtn: ConstraintLayout
    private lateinit var searchEditText: EditText
    lateinit var searchRecyclerView: RecyclerView
    private lateinit var itemAdapter: ItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        backBtn=view.findViewById(R.id.backBtn)
        searchEditText=view.findViewById(R.id.searchEditText)
        searchRecyclerView=view.findViewById(R.id.searchRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemAdapter = ItemsAdapter(
            requireContext(),
            mutableListOf(), // Start with an empty list
            { article -> // Handle item click here for reading
                val intent = Intent(requireContext(), ReadingActivity::class.java).apply {
                    putExtra("author", article.author)
                    putExtra("content", article.content)
                    putExtra("description", article.description)
                    putExtra("publishedAt", article.publishedAt)
                    putExtra("title", article.title)
                    putExtra("urlToImage", article.urlToImage)
                }
                startActivity(intent)
            },
            this
        )
        searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = itemAdapter
        }

        setupSearch()

        backBtn.setOnClickListener {
            activity?.finish()
        }
    }
    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                fetchNews(s.toString())
            }
        })
    }
    private fun fetchNews(query: String) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()
        val call=retroIns.create(ApiInterface::class.java)
        if (query.isNotEmpty()) {
            call.searchNews(query, "46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :
                Callback<Data1> {
                override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val articles = it.articles.toMutableList()
                            Log.d("SearchFragment", "Articles fetched: ${articles.size}")
                            itemAdapter.updateArticles(articles)
                        }
                    }
                }

                override fun onFailure(call: Call<Data1>, t: Throwable) {
                    Log.e("SearchFragment", "Response failed: ${t.message}")
                }
            })
        } else {
            // Clear the adapter's data when search text is empty
            itemAdapter.updateArticles(mutableListOf())
        }
    }

    override fun onFavoriteSelected(article: Data1.Article) {

//        val sharedPreferences = requireActivity().getSharedPreferences("article_data", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        editor.putString("author", article.author)
//        editor.putString("title", article.title)
//        editor.putString("content", article.content)
//        editor.putString("publishedAt", article.publishedAt)
//        editor.putString("description", article.description)
//        editor.putString("urlToImage", article.urlToImage)
//
//        editor.apply()
        val gson = Gson()
        val sharedPreferences = requireActivity().getSharedPreferences("article_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Retrieve existing article list from SharedPreferences
        val articleListJson = sharedPreferences.getString("articleList", null)
        val type = object : TypeToken<List<Data1.Article>>() {}.type
        val existingArticleList: MutableList<Data1.Article> = gson.fromJson(articleListJson, type) ?: mutableListOf()

        // Add the new article to the existing list
        existingArticleList.add(article)

        // Convert the updated list to JSON string
        val updatedArticleListJson = gson.toJson(existingArticleList)

        // Store the updated JSON string in SharedPreferences
        editor.putString("articleList", updatedArticleListJson)
        editor.apply()
        Toast.makeText(requireContext(), "Added successfully", Toast.LENGTH_SHORT).show()
    }
}