package com.example.catastrophic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.browsing.BrowseCatsViewModel
import com.example.browsing.Cat
import com.example.browsing.databinding.ActivityBrowseCatsBinding
import com.example.catastrophic.databinding.ActivityMainBinding
import com.example.core.utils.NetworkHelper
import com.example.core.utils.Resource
import org.koin.android.ext.android.inject
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val browseViewModel: BrowseCatsViewModel by viewModels()
    private val networkHelper: NetworkHelper by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        browseViewModel.catsLiveData.observe(this, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> {
                    val s = it.data?.fold(StringBuilder(), { acc: StringBuilder, cat: Cat ->
                        acc.append( "${cat.id ?: "id"} - " )
                    }).toString()
                    Toast.makeText(this@MainActivity, s, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        networkHelper.connectivityLiveData.observe(this, Observer {
          it?.let {
              browseViewModel.fetchCats()
          }
        })
    }
}