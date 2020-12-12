package com.example.catastrophic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.browsing.BrowseCatsViewModel
import com.example.browsing.databinding.ActivityBrowseCatsBinding
import com.example.catastrophic.databinding.ActivityMainBinding
import com.example.core.utils.Resource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val browseViewModel: BrowseCatsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        browseViewModel.catsLiveData.observe(this, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> Toast.makeText(this@MainActivity, "${it.data?.size ?: 0}", Toast.LENGTH_SHORT).show()
                else -> {
                    Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        browseViewModel.fetchCats()
    }
}