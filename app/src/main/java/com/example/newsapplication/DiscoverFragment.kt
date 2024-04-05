package com.example.newsapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.Adapter.secondCatAdapter
import com.example.newsapplication.DataClass.secondCatDataClass



class DiscoverFragment : Fragment() {
lateinit var secondCategoryRecyclerView:RecyclerView
lateinit var searchLIstner:CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)
        secondCategoryRecyclerView=view.findViewById(R.id.secondCategoryRecyclerView)
        searchLIstner=view.findViewById(R.id.searchLIstner)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchLIstner.setOnClickListener{
            val intent=Intent(requireContext(),SearchActivity::class.java)
            startActivity(intent)
        }

        val secondArr=ArrayList<secondCatDataClass>()
        secondArr.add(secondCatDataClass(R.color.brown,R.drawable.business_icon,"Business"))
        secondArr.add(secondCatDataClass(R.color.green,R.drawable.entertainment_icon,"Entertainment"))
        secondArr.add(secondCatDataClass(R.color.blue,R.drawable.general_icon,"General"))
        secondArr.add(secondCatDataClass(R.color.navyBlue,R.drawable.health_icon,"Health"))
        secondArr.add(secondCatDataClass(R.color.orange,R.drawable.science_icon,"Science"))
        secondArr.add(secondCatDataClass(R.color.pink,R.drawable.sports_icon,"Sports"))
        secondArr.add(secondCatDataClass(R.color.pink,R.drawable.texhnology_icon,"Technology"))

        val layoutManager = GridLayoutManager(context, 3)
        secondCategoryRecyclerView.layoutManager = layoutManager
        val adapter=secondCatAdapter(secondArr){ categoryName ->
            // Handle item click here and navigate to the appropriate fragment
            when (categoryName) {
                "Business" -> navigateToFragment(categoryName)
                "Entertainment" -> navigateToFragment(categoryName)
                "General" -> navigateToFragment(categoryName)
                "Health" -> navigateToFragment(categoryName)
                "Science" -> navigateToFragment(categoryName)
                "Sports" -> navigateToFragment(categoryName)
                "Technology" -> navigateToFragment(categoryName)
            }
        }
        secondCategoryRecyclerView.adapter=adapter

    }
    private fun navigateToFragment(categoryName:String) {
        val intent = Intent(requireContext(), ListCategoryActivity::class.java)
        intent.putExtra("categoryName", categoryName)
        startActivity(intent)
    }


}