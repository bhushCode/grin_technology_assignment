package com.example.tesstingapplication02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.tesstingapplication02.adapter.ItemAdapter
import com.example.tesstingapplication02.databinding.ActivityMainBinding
import com.example.tesstingapplication02.service.ApiInterface
import com.example.tesstingapplication02.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<ItemPage.Data>
    private var page = 1
    private var limit = 5
    private var isLoading = false
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        arrayList = ArrayList()
        adapter = ItemAdapter(arrayList,this@MainActivity)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager =
                    recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = adapter.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    page++

                    pageLoad()
                    binding.swiplayout.isRefreshing=false
                }
            }
        })

        binding.swiplayout.setOnRefreshListener {

            if(NetworkUtils.isInternetAvailable(this@MainActivity))
            {
                page= 1
                arrayList.clear()
                pageLoad()
                binding.ll1.visibility=View.VISIBLE
                binding.txtSwipeUp.visibility=View.GONE
                Log.d("HUla","swipup")

            }
            else{
                binding.ll1.visibility=View.INVISIBLE
                binding.txtSwipeUp.visibility=View.VISIBLE
                binding.swiplayout.isRefreshing=false
                Toast.makeText(this@MainActivity,"No internet",Toast.LENGTH_LONG).show()
            }


        }

        if(NetworkUtils.isInternetAvailable(this@MainActivity))
        {
            pageLoad()
        }
        else{
            binding.ll1.visibility=View.INVISIBLE
            binding.txtSwipeUp.visibility=View.VISIBLE
            Toast.makeText(this@MainActivity,"No internet",Toast.LENGTH_LONG).show()

        }

    }

    private fun pageLoad() {
        binding.progressbar.visibility = View.VISIBLE
        isLoading = true

        Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
            .getItems(page)
            .enqueue(object : Callback<ItemPage> {
                override fun onResponse(call: Call<ItemPage>, response: Response<ItemPage>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            arrayList.addAll(it.data)
                            adapter.notifyDataSetChanged()
                        }
                        binding.progressbar.visibility = View.INVISIBLE
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<ItemPage>, t: Throwable) {
                    binding.progressbar.visibility = View.INVISIBLE
                    Log.d("MainActivity", t.message.toString())
                    isLoading = false
                }
            })
    }
}
