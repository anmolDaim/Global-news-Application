package com.example.newsapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.Adapter.ItemsAdapter
import com.example.newsapplication.Adapter.videoAdapter
import com.example.newsapplication.DataClass.Data1
import com.example.newsapplication.DataClass.videoDataClass
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class VideoFragment : Fragment() {

lateinit var videoRecyclerView:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        videoRecyclerView=view.findViewById(R.id.videoRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retroIns= Retrofit.Builder().baseUrl("https://newsapi.org/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build()

        val call=retroIns.create(ApiInterface::class.java)
        call.getApiData("us","46bbdd49ab6148fbb7c6091ef59e42d2").enqueue(object : Callback<Data1> {
            override fun onResponse(call: Call<Data1>, response: Response<Data1>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val videoList = responseBody.articles.map { article ->
                            videoDataClass(
                                videoImage = article.urlToImage ?: "", // Use empty string or a placeholder if null
                                videoHeading = article.title ?: "No Title",
                                videoDate = article.publishedAt ?: "No Date",
                                content = article.content ?: "No Content"
                            )
                        }.let { ArrayList(it) }

                        val linearLayout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        videoRecyclerView.layoutManager = linearLayout
                        val adapter = videoAdapter(videoList)
                        videoRecyclerView.adapter = adapter

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
}