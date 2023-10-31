package com.example.waystoryapp.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.waystoryapp.R
import com.example.waystoryapp.data.response.ListStoryItem
import com.example.waystoryapp.data.tools.StoryListAdapter
import com.example.waystoryapp.databinding.ActivityMainBinding
import com.example.waystoryapp.view.ViewModelFactory
import com.example.waystoryapp.view.login.LoginActivity
import com.example.waystoryapp.view.story.add.AddStoryActivity
import com.example.waystoryapp.view.story.detail.DetailStoryActivity

class MainActivity : AppCompatActivity(), StoryListAdapter.OnItemClickListener {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { setting ->
            if (setting.token.isNotEmpty()) {
                Log.i("ListStoryActivity", "setupAction: ${setting.token}")
                viewModel.fetchListStories(setting.token)
            }
        }

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rvListStory)
        val adapter = StoryListAdapter(this)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.listStories.observe(this) { items ->
            adapter.submitList(items)
        }
        setupAction()
    }


    private fun setupAction() {
        binding.imgLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onItemClick(item: ListStoryItem) {
        val id = item.id
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.EXTRA_NAME, id)
        startActivity(intent)
    }
}