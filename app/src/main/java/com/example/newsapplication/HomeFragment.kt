package com.example.newsapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapplication.Adapter.ImageAdapter
import com.example.newsapplication.Adapter.ItemsAdapter
import com.example.newsapplication.Adapter.categoryAdapter
import com.example.newsapplication.DataClass.Data1
import com.example.newsapplication.DataClass.categoryDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level
import kotlin.math.abs


class HomeFragment : Fragment(), ItemsAdapter.OnFavoriteSelectedListener {
    private lateinit var cont: FragmentActivity
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageAdapter
    lateinit var newsRecyclerView:RecyclerView
    lateinit var CategoryAdapter:categoryAdapter
    lateinit var progressBar:ProgressBar
    //lateinit var favouriteNews:ImageView
    lateinit var categoryRecyclerView:RecyclerView
    private lateinit var article: Data1.Article
    private val gson = Gson()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var notiConstraintLayout:ConstraintLayout
    lateinit var searchConstraintLayout:ConstraintLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentActivity){
             cont=context
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView)
        //favouriteNews = view.findViewById(R.id.favouriteNews)
        viewPager2 = view.findViewById(R.id.viewPager2) ?: ViewPager2(requireContext())
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        progressBar=view.findViewById(R.id.progressBar)
        notiConstraintLayout=view.findViewById(R.id.notiConstraintLayout)
        searchConstraintLayout=view.findViewById(R.id.searchConstraintLayout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        notiConstraintLayout.setOnClickListener {
            val intent=Intent(cont,NotificationActivity::class.java)
            startActivity(intent)
        }
        searchConstraintLayout.setOnClickListener {
            val intent=Intent(cont,SearchActivity::class.java)
            startActivity(intent)
        }

        progressBar.visibility = View.VISIBLE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.getApiData("us","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )
                        newsRecyclerView.adapter = catAdapter
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


        var catArr=ArrayList<categoryDataClass>()
        catArr.add(categoryDataClass("Hot News"))
        catArr.add(categoryDataClass("Business"))
        catArr.add(categoryDataClass("Entertainment"))
        catArr.add(categoryDataClass("General"))
        catArr.add(categoryDataClass("Health"))
        catArr.add(categoryDataClass("Science"))
        catArr.add(categoryDataClass("Sports"))
        catArr.add(categoryDataClass("Technology"))
        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.layoutManager = linearLayout
        CategoryAdapter = categoryAdapter(cont,catArr){categoryName->
            when (categoryName) {
                "Hot News" -> fetchTopHeadlines()
                "Business" -> fetchBusinessNews("business")
                "Entertainment" -> fetchEntertainmentNews("entertainment")
                "General" -> fetchGeneralNews()
                "Health"-> fetchHealthNews()
                "Science" -> fetchScienceNews()
                "Sports" -> fetchSportsNews()
                "Technology"->fetchTechnologyyNews()

            }
        }
        categoryRecyclerView.adapter = CategoryAdapter

        viewPager2 = view?.findViewById(R.id.viewPager2) ?: ViewPager2(requireContext())

        init()
        setTransformer()


    }


    private fun fetchTechnologyyNews(){


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","technology","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )
                        newsRecyclerView.adapter = catAdapter

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

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","sports","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )
                        newsRecyclerView.adapter = catAdapter

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

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","science","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )
                        newsRecyclerView.adapter = catAdapter

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

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","health","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )
                        newsRecyclerView.adapter = catAdapter

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

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","general","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )
                        newsRecyclerView.adapter = catAdapter

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

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","entertainment","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )
                        newsRecyclerView.adapter = catAdapter

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

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.categoryApiData("us","business","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )
                        newsRecyclerView.adapter = catAdapter

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

    private fun fetchTopHeadlines() {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.getApiData("us","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object :Callback<Data1>{
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val productList = it.articles.toMutableList()
                        val linearLayout = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL, false)
                        newsRecyclerView.layoutManager = linearLayout
                        val catAdapter = ItemsAdapter(
                            cont,
                            productList,
                            { article ->
                                // Handle item click here for reading
                                val intent = Intent(context, ReadingActivity::class.java).apply {
                                    putExtra("author", article.author)
                                    putExtra("content", article.content)
                                    putExtra("description", article.description)
                                    putExtra("publishedAt", article.publishedAt)
                                    putExtra("title", article.title)
                                    putExtra("urlToImage", article.urlToImage)
                                }
                                startActivity(intent)
                            },
                            this@HomeFragment
                        )

                        newsRecyclerView.adapter = catAdapter

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

    private fun setTransformer() {
        val transformer= CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r=1-abs(position)
            page.scaleY=0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)

        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable,2000)
            }
        })
    }

    private fun init(){
        viewPager2 = view?.findViewById(R.id.viewPager2) ?: ViewPager2(requireContext())
        handler = android.os.Handler(Looper.myLooper()!!)

        progressBar.visibility = View.VISIBLE
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns=Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(GsonConverterFactory.create()).build()


        // Your Retrofit call to fetch API data
        val call = retroIns.create(ApiInterface::class.java)
        call.getApiData("us", "46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object : Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val articleList = it.articles.toMutableList()

                        // Initialize the adapter with the list of articles and set it to ViewPager2
                        val adapter = ImageAdapter(ArrayList(articleList),viewPager2) { article ->
                            // Handle item click here
                            val intent = Intent(context, ReadingActivity::class.java)
                            intent.putExtra("author", article.author)
                            intent.putExtra("content", article.content)
                            intent.putExtra("description", article.description)
                            intent.putExtra("publishedAt", article.publishedAt)
                            intent.putExtra("title", article.title)
                            intent.putExtra("urlToImage", article.urlToImage)
                            startActivity(intent)
                        }
                        viewPager2.adapter = adapter

                        progressBar.visibility = View.GONE
                    } ?: Log.e("HomeFragment", "Response body is null")
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.code()}")
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Data1>, t: Throwable) {
                Log.e("HomeFragment", "API call failed: ${t.message}")
                progressBar.visibility = View.GONE
            }
        })

        viewPager2.offscreenPageLimit=3
        viewPager2.clipToPadding=false
        viewPager2.clipChildren=false
        viewPager2.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER

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

        Toast.makeText(cont, "Added successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,2000)
    }
    private val runnable= Runnable {
        viewPager2.currentItem=viewPager2.currentItem+1
    }
}