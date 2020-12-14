package com.example.browsing

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browsing.databinding.ActivityBrowseCatsBinding
import com.example.core.utils.NetworkHelper
import com.example.core.utils.Resource
import org.koin.android.ext.android.inject
import java.lang.StringBuilder

class BrowseCatsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityBrowseCatsBinding

    private val browseViewModel: BrowseCatsViewModel by viewModels()
    private val catsAdapter = CatsAdapter()
    private val networkHelper: NetworkHelper by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseCatsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        configureRecyclerView()

        browseViewModel.catsLiveData.observe(this, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> {
                    val s = it.data?.fold(StringBuilder(), { acc: StringBuilder, cat: Cat ->
                        acc.append( "${cat.id ?: "id"} - " )
                    }).toString()
                    catsAdapter.replaceItems(it.data ?: emptyList())
                }
                else -> {
                    // Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        if (savedInstanceState == null) {
            networkHelper.connectivityLiveData.observe(this, Observer {
                it?.let {
                    browseViewModel.fetchCats()
                }
            })
        }
    }

    private fun configureRecyclerView() {
        binding.list.adapter = catsAdapter
        binding.list.layoutManager = GridLayoutManager(this, 3)
        binding.list.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                    val lastItemIndex =
                        (binding.list.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    if (lastItemIndex > catsAdapter.itemCount - 6
                        && networkHelper.connectivityLiveData.value == true
                        && browseViewModel.catsLiveData.value?.status != Resource.Status.LOADING) {
                        browseViewModel.fetchCats()
                        Toast.makeText(
                            this@BrowseCatsActivity,
                            "$lastItemIndex",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }
}