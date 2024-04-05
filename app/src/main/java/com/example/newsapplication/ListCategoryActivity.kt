package com.example.newsapplication

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.Adapter.ItemsAdapter
import com.example.newsapplication.DataClass.Data1
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListCategoryActivity : AppCompatActivity(), ItemsAdapter.OnFavoriteSelectedListener {
    lateinit var headingName:TextView
    lateinit var listCategoryRecyclerView:RecyclerView
    lateinit var backBtn:ImageView
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_category)
        headingName=findViewById(R.id.headingName)
        listCategoryRecyclerView=findViewById(R.id.listCategoryRecyclerView)
        progressBar=findViewById(R.id.progressBar)
        backBtn=findViewById(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

        val categoryName = intent.getStringExtra("categoryName")

        headingName.text=categoryName
        when (categoryName) {
            "Business" -> fetchBusinessNews("business")
            "Entertainment" -> fetchEntertainmentNews("entertainment")
            "General" -> fetchGeneralNews()
            "Health"-> fetchHealthNews()
            "Science" -> fetchScienceNews()
            "Sports" -> fetchSportsNews()
            "Technology"->fetchTechnologyyNews()

        }
    }
    private fun fetchTechnologyyNews(){


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","technology","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :
            Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(this@ListCategoryActivity, LinearLayoutManager.VERTICAL, false)
                        listCategoryRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            this@ListCategoryActivity,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(this@ListCategoryActivity, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@ListCategoryActivity
                        )
                        listCategoryRecyclerView.adapter = catAdapter

                        progressBar.visibility = View.GONE
                    } ?: Log.e("HomeFragment", "Response body is null")
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.code()}")
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Data1>, t: Throwable) {
                Log.e("HomeFragment", "API call failed: ${t.message}")
            }


        })
    }
    private fun fetchSportsNews(){


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","sports","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :
            Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(this@ListCategoryActivity, LinearLayoutManager.VERTICAL, false)
                        listCategoryRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            this@ListCategoryActivity,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(this@ListCategoryActivity, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@ListCategoryActivity
                        )
                        listCategoryRecyclerView.adapter = catAdapter

                    } ?: Log.e("HomeFragment", "Response body is null")
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Data1>, t: Throwable) {
                Log.e("HomeFragment", "API call failed: ${t.message}")
                progressBar.visibility = View.GONE
            }


        })
    }

    private fun fetchScienceNews(){


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","science","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :
            Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(this@ListCategoryActivity, LinearLayoutManager.VERTICAL, false)
                        listCategoryRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            this@ListCategoryActivity,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(this@ListCategoryActivity, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@ListCategoryActivity
                        )
                        listCategoryRecyclerView.adapter = catAdapter

                    } ?: Log.e("HomeFragment", "Response body is null")
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.code()}")

                }
            }

            override fun onFailure(call: Call<Data1>, t: Throwable) {
                Log.e("HomeFragment", "API call failed: ${t.message}")
            }


        })
    }

    private fun fetchHealthNews() {


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","health","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :
            Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(this@ListCategoryActivity, LinearLayoutManager.VERTICAL, false)
                        listCategoryRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            this@ListCategoryActivity,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(this@ListCategoryActivity, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@ListCategoryActivity
                        )
                        listCategoryRecyclerView.adapter = catAdapter

                    } ?: Log.e("HomeFragment", "Response body is null")
                } else {
                    Log.e("HomeFragment", "RespoprogressBar.visibility = View.GONE")

                }
            }

            override fun onFailure(call: Call<Data1>, t: Throwable) {
                Log.e("HomeFragment", "API call failed: ${t.message}")
            }


        })
    }

    private fun fetchGeneralNews() {


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","general","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :
            Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(this@ListCategoryActivity, LinearLayoutManager.VERTICAL, false)
                        listCategoryRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            this@ListCategoryActivity,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(this@ListCategoryActivity, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@ListCategoryActivity
                        )
                        listCategoryRecyclerView.adapter = catAdapter

                    } ?: Log.e("HomeFragment", "Response body is null")
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.code()}")

                }
            }

            override fun onFailure(call: Call<Data1>, t: Throwable) {
                Log.e("HomeFragment", "API call failed: ${t.message}")
            }


        })
    }

    private fun fetchEntertainmentNews(s: String) {


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","entertainment","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :
            Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(this@ListCategoryActivity, LinearLayoutManager.VERTICAL, false)
                        listCategoryRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            this@ListCategoryActivity,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(this@ListCategoryActivity, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@ListCategoryActivity
                        )
                        listCategoryRecyclerView.adapter = catAdapter

                    } ?: Log.e("HomeFragment", "Response body is null")
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.code()}")

                }
            }

            override fun onFailure(call: Call<Data1>, t: Throwable) {
                Log.e("HomeFragment", "API call failed: ${t.message}")
            }


        })
    }

    private fun fetchBusinessNews(s: String) {


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","business","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :
            Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(this@ListCategoryActivity, LinearLayoutManager.VERTICAL, false)
                        listCategoryRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            this@ListCategoryActivity,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(this@ListCategoryActivity, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@ListCategoryActivity
                        )
                        listCategoryRecyclerView.adapter = catAdapter

                    } ?: Log.e("HomeFragment", "Response body is null")
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.code()}")

                }
            }

            override fun onFailure(call: Call<Data1>, t: Throwable) {
                Log.e("HomeFragment", "API call failed: ${t.message}")
            }


        })
    }


    override fun onFavoriteSelected(article: Data1.Article) {
        val bundle = Bundle().apply {
            putString("author", article.author)
            putString("title", article.title)
            putString("content", article.content)
            putString("publishedAt", article.publishedAt)
            putString("description", article.description)
            putString("urlToImage", article.urlToImage)
        }
        val savedFragment = SavedFragment().apply {
            arguments = bundle
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, savedFragment)
            addToBackStack(null)
            commit()
        }
    }

}