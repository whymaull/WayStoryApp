package com.example.waystoryapp.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.waystoryapp.R
import com.example.waystoryapp.data.database.Entities
import com.example.waystoryapp.data.response.ListStoryItem
import com.example.waystoryapp.data.tools.LoadStateAdaprer
import com.example.waystoryapp.data.tools.StoryListAdapter
import com.example.waystoryapp.databinding.ActivityMainBinding
import com.example.waystoryapp.view.ViewModelFactory
import com.example.waystoryapp.view.login.LoginActivity
import com.example.waystoryapp.view.maps.MapsActivity
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

//        viewModel.getSession().observe(this) { setting ->
//            if (setting.token.isNotEmpty()) {
//                Log.i("ListStoryActivity", "setupAction: ${setting.token}")
//                viewModel.fetchListStories(setting.token)
//            }
//        }
        binding.rvListStory.layoutManager = LinearLayoutManager(this)
//        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rvListStory)
//        val adapter = StoryListAdapter(this)
//
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter
//
//        viewModel.isLoading.observe(this) { isLoading ->
//            if (isLoading) {
//                binding.progressBar.visibility = View.VISIBLE
//            } else {
//                binding.progressBar.visibility = View.GONE
//            }
//        }

//        viewModel.listStories.observe(this) { items ->
//            adapter.submitList(items)
//        }
        getData()

        setupAction()
    }

    private fun getData() {
        val adapter = StoryListAdapter(this) // Create an instance of ListStoryAdapter
        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadStateAdaprer {
                adapter.retry()
            }
        )
        viewModel.qute.observe(this) {
            adapter.submitData(lifecycle, it)
        }
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

        binding.imgMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onItemClick(item: Entities) {
        val id = item.idStory
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.EXTRA_NAME, id)
        startActivity(intent)
    }
}