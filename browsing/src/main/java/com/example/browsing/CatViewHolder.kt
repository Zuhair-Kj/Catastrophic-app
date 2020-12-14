package com.example.browsing

import androidx.recyclerview.widget.RecyclerView
import com.example.browsing.databinding.ItemCatBinding

class CatViewHolder constructor(private val binding: ItemCatBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(cat: Cat) {
        binding.cat = cat
        binding.executePendingBindings()
    }
}