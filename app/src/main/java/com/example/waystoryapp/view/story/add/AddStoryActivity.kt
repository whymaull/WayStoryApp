package com.example.waystoryapp.view.story.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.waystoryapp.R
import com.example.waystoryapp.data.tools.reduceFileImage
import com.example.waystoryapp.data.tools.uriToFile
import com.example.waystoryapp.databinding.ActivityAddStoryBinding
import com.example.waystoryapp.view.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Suppress("DEPRECATION")
class AddStoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null

    private lateinit var binding: ActivityAddStoryBinding

    private val cameraPermissionRequest = 100
    private val requestImageCapture = 1
    private var currentImageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) {
            load(it)
        }
        initAction()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            // Gambar yang diambil dari kamera akan ada dalam data
            currentImageBitmap = data?.extras?.get("data") as Bitmap

            // Set gambar ke ImageView
            binding.imgPreview.setImageBitmap(currentImageBitmap)
        }
    }

    private fun initAction() {

        binding.btnOpenCamera.setOnClickListener{
            if (checkCameraPermission()) {
                // Izin kamera sudah diberikan
                initCameraButton()
            } else {
                // Izin kamera belum diberikan, maka tampilkan permintaan izin
                requestCameraPermission()
            }
        }

        binding.btnOpenGallery.setOnClickListener{
            startGallery()
        }

        binding.btnUpload.setOnClickListener{
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                val description = binding.edtStoryDesc.text.toString()

                viewModel.getSession().observe(this) { setting ->
                    if (setting.token.isNotEmpty()) {
                        Log.i("AddStoryActivity", "setupAction: ${setting.token}")
                        val imgPart = MultipartBody.Part.createFormData("photo", imageFile.name, RequestBody.create("image/*".toMediaTypeOrNull(), imageFile))
                        viewModel.addStory(setting.token, imgPart, description)
                    }
                }
            }
        }

    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Menampilkan pesan kepada pengguna mengapa izin kamera diperlukan
            // Ini hanya akan ditampilkan jika pengguna telah menolak izin sebelumnya
            showPermissionExplanation()
        } else {
            // Meminta izin kamera langsung
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraPermissionRequest)
        }
    }

    private fun showPermissionExplanation() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Aplikasi ini memerlukan izin kamera untuk mengambil foto.")
            .setPositiveButton("Izinkan") { dialog, id ->
                ActivityCompat.requestPermissions(this@AddStoryActivity, arrayOf(Manifest.permission.CAMERA), cameraPermissionRequest)
            }
            .setNegativeButton("Tolak") { dialog, id ->
                // Izin kamera ditolak oleh pengguna
                // Anda dapat memberikan pemberitahuan kepada pengguna di sini
            }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequest) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin kamera telah diberikan
                initCameraButton()
            } else {
                // Izin kamera ditolak oleh pengguna
                // Anda dapat memberikan pemberitahuan kepada pengguna di sini
            }
        }
    }

    private fun initCameraButton() {
        findViewById<View>(R.id.btnOpenCamera).setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, requestImageCapture)
        } else {
            // Aplikasi kamera tidak ditemukan
            Toast.makeText(this, "Aplikasi kamera tidak tersedia.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
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