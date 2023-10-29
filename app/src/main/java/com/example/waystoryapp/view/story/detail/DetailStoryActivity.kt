package com.example.waystoryapp.view.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.waystoryapp.R

class DetailStoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)
    }


    companion object {
        const val EXTRA_NAME = "extra_name"
    }
}