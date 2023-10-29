package com.example.waystoryapp.view.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.waystoryapp.data.response.Story
import com.example.waystoryapp.databinding.ActivityDetailStoryBinding
import com.example.waystoryapp.view.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_NAME)
        Log.i("DetailStoryActivity", "onCreate: $id")
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        if (id != null) {
            viewModel.getSession().observe(this) { setting ->
                if (setting.token.isNotEmpty()) {
                    Log.i("ListStoryActivity", "setupAction: ${setting.token}")
                    viewModel.getStory(setting.token, id)
                }
            }


        }

        viewModel.getStories.observe(this) { result ->
            setUser(result)
        }
    }

    private fun setUser(result: Story) {
        binding.apply {
            tvName.text = "${result.name}"
            tvDesc.text = result.description
            Glide.with(this@DetailStoryActivity)
                .load(result.photoUrl)
                .into(imgStoryDetail)
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
    }
}