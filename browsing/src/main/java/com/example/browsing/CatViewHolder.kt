package com.example.browsing

import androidx.recyclerview.widget.RecyclerView
import com.example.browsing.databinding.ItemCatBinding
import org.koin.core.KoinComponent
import org.koin.core.inject

class CatViewHolder constructor(private val binding: ItemCatBinding): RecyclerView.ViewHolder(binding.root), KoinComponent {
    private val catClickedListener: BrowseCatsFragment.OnCatSelectedListener by inject()
    fun bind(cat: Cat) {
        binding.cat = cat
        binding.executePendingBindings()
        binding.image.transitionName = binding.root.context.getString(R.string.image_transition_name, cat.id)
        binding.root.setOnClickListener {
            catClickedListener.onCatSelected(cat, binding.image)
        }
    }
}