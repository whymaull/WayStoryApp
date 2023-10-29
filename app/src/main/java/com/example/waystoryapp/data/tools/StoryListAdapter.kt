package com.example.waystoryapp.data.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waystoryapp.data.response.ListStoryItem
import com.example.waystoryapp.databinding.ListStoryBinding

class StoryListAdapter (private val onItemClickListener: OnItemClickListener) :
    ListAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickListener {
        fun onItemClick(item: ListStoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,onItemClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
    class MyViewHolder(
        private val binding: ListStoryBinding,
        private val onItemClickListener: OnItemClickListener

    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ListStoryItem) {
            binding.tvStoryTitle.text = "${review.name}"
            binding.tvStoryDesc.text = review.description
            Glide.with(binding.root.context)
                .load(review.photoUrl)
                .into(binding.imgStory)

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(review)
            }
        }
    }

    companion object {
        const val TAG = "UserAdapter"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}