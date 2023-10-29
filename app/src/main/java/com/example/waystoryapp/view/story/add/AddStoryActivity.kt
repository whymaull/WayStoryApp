package com.example.waystoryapp.view.story.add

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.waystoryapp.R
import com.example.waystoryapp.data.tools.getImageUri
import com.example.waystoryapp.data.tools.reduceFileImage
import com.example.waystoryapp.data.tools.uriToFile
import com.example.waystoryapp.databinding.ActivityAddStoryBinding
import com.example.waystoryapp.view.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null

    private lateinit var binding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) {
            load(it)
        }
        initAction()
    }

    private fun initAction() {

        binding.btnOpenCamera.setOnClickListener{
            startCamera()
        }

        binding.btnOpenGallery.setOnClickListener{
            startGallery()
        }

        binding.btnUpload.setOnClickListener{
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                val description = binding.edtStoryDesc.text.toString()



                val requestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val image = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

                val imageMultiPart = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    image
                )
                viewModel.getSession().observe(this) { setting ->
                    if (setting.token.isNotEmpty()) {
                        Log.i("AddStoryActivity", "setupAction: ${setting.token}")
                        viewModel.addStory(setting.token, file = imageMultiPart)
                    }
                }
            }
        }

    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()

        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgPreview.setImageURI(it)
        }

    }

    private fun load(result: Boolean) {
        if (result) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}