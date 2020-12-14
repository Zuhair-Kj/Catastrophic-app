package com.example.browsing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browsing.databinding.ItemCatBinding

class CatsAdapter(private val items: MutableList<Cat> = mutableListOf()): RecyclerView.Adapter<CatViewHolder>() {

    fun replaceItems(newItems: List<Cat>) {
        val indexDiff = newItems.size - itemCount
        items.addAll(newItems.subList(itemCount, newItems.size))
        notifyItemRangeInserted(itemCount - indexDiff, itemCount - 1)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding = ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        (binding.root.layoutParams as? GridLayoutManager.LayoutParams)?.let {
            val width = parent.measuredWidth / 3
            it.width = width
            it.height = width
        }
        return CatViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(items[position])
    }
}