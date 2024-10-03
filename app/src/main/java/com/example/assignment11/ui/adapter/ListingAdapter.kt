package com.example.assignment11.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment11.databinding.ListItemBinding
import com.example.assignment11.domain.Articles

class ListingAdapter(private val clickListener: NewsListener) :
    ListAdapter<Articles,ListingAdapter.ListingViewHolder>(NewsCallback()) {

    inner class ListingViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Articles, position: Int, clickListener: NewsListener) {
            binding.viewModel = article
            binding.clickListener = clickListener
            if (position % 4 == 0 || position % 4 == 2) {
                this.itemView.layoutParams.height = 600
            } else {
                this.itemView.layoutParams.height = 400
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        return ListingViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        holder.bind(getItem(position), position, clickListener)
    }

}

class NewsCallback : DiffUtil.ItemCallback<Articles>()  {
    override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
        return oldItem == newItem
    }
}

class NewsListener(val clickListener: (articleUrl: String) -> Unit) {
    fun onClick(article: Articles) = clickListener(article.url)
}