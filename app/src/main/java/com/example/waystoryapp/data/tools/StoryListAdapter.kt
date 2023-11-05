package com.example.waystoryapp.data.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waystoryapp.data.database.Entities
import com.example.waystoryapp.data.response.ListStoryItem
import com.example.waystoryapp.databinding.ListStoryBinding

class StoryListAdapter (private val onItemClickListener: OnItemClickListener) :
    PagingDataAdapter<Entities, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickListener {
        fun onItemClick(item: Entities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,onItemClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }
    class MyViewHolder(
        private val binding: ListStoryBinding,
        private val onItemClickListener: OnItemClickListener

    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Entities) {
            binding.tvStoryTitle.text = "${review.sender}"
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Entities>() {
            override fun areItemsTheSame(oldItem: Entities, newItem: Entities): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Entities,
                newItem: Entities,
            ): Boolean {
                return oldItem.idStory == newItem.idStory
            }
        }
    }

}