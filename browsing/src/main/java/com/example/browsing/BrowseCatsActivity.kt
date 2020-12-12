package com.example.browsing

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.browsing.databinding.ActivityBrowseCatsBinding
import com.example.core.utils.Resource

class BrowseCatsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityBrowseCatsBinding

    private val browseViewModel: BrowseCatsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseCatsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        browseViewModel.catsLiveData.observe(this, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> Toast.makeText(this@BrowseCatsActivity, "${it.data?.size ?: 0}", Toast.LENGTH_SHORT).show()
                else -> {

                }
            }
        })
    }
}