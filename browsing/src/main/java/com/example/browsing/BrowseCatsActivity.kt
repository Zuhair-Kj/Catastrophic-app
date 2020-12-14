package com.example.browsing

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browsing.databinding.ActivityBrowseCatsBinding
import com.example.core.utils.NetworkHelper
import com.example.core.utils.Resource
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import java.lang.StringBuilder

class BrowseCatsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityBrowseCatsBinding

    private val browseViewModel: BrowseCatsViewModel by viewModels()
    private val catsAdapter = CatsAdapter()
    private val networkHelper: NetworkHelper by inject()
    private var tmpIsOnline: Boolean? = null
    private val onlineSnackbar by lazy {
        Snackbar.make(binding.coordinatorLayout, R.string.online, Snackbar.LENGTH_SHORT).also {
            it.view.setBackgroundColor(ContextCompat.getColor(it.context, R.color.colorPrimaryDark))
        }
    }
    private val offlineSnackbar by lazy {
        Snackbar.make(binding.coordinatorLayout, R.string.offline, Snackbar.LENGTH_INDEFINITE).also {
            it.view.setBackgroundColor(ContextCompat.getColor(it.context, R.color.colorPrimaryDark))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseCatsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        configureRecyclerView()

        browseViewModel.catsLiveData.observe(this, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> {
                    binding.lottieLoader.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                    val s = it.data?.fold(StringBuilder(), { acc: StringBuilder, cat: Cat ->
                        acc.append( "${cat.id ?: "id"} - " )
                    }).toString()
                    catsAdapter.replaceItems(it.data ?: emptyList())
                }
                Resource.Status.LOADING -> {
                    if (catsAdapter.itemCount == 0) {
                        binding.lottieLoader.visibility = View.VISIBLE
                        binding.list.visibility = View.GONE
                        binding.offlineGroup.visibility = View.GONE
                    }
                }
                Resource.Status.ERROR -> {
                    binding.lottieLoader.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                    if (catsAdapter.itemCount == 0)
                        binding.offlineGroup.visibility = View.VISIBLE
                }
            }
        })

        if (savedInstanceState == null) {
            networkHelper.connectivityLiveData.observe(this, Observer {
                it?.let { isOnline ->
                    if (isOnline ) {
                        if (tmpIsOnline == false) {
                            onlineSnackbar.show()
                            offlineSnackbar.dismiss()
                            catsAdapter.clear()
                        }
                    } else {
                        onlineSnackbar.dismiss()
                        offlineSnackbar.show()
                    }
                    browseViewModel.fetchCats()
                }
                tmpIsOnline = it
            })
        }

        binding.buttonRetry.setOnClickListener {
            browseViewModel.fetchCats()
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