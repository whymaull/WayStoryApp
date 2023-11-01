package com.example.waystoryapp.view.story.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.example.waystoryapp.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) {
            load(it)
        }

        viewModel.uploadSuccess.observe(this) { success ->
            if (success) {
                // Proses upload berhasil, pindahkan ke MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
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
                dispatchTakePictureIntent()
            } else {
                // Izin kamera belum diberikan, tampilkan permintaan izin
                requestCameraPermission()
            }
        }

        binding.btnOpenGallery.setOnClickListener{
            startGallery()
        }

        binding.btnUpload.setOnClickListener{
            val description = binding.edtStoryDesc.text.toString()
            val requestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

            if (currentImageUri == null && currentImageBitmap == null) {
                Toast.makeText(this, "Silakan ambil foto terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else if (description.isEmpty()) {
                Toast.makeText(this, "Silakan isi deskripsi cerita", Toast.LENGTH_SHORT).show()
            } else {
                val imageFile = currentImageUri?.let { uri ->
                    uriToFile(uri, this).reduceFileImage()
                } ?: currentImageBitmap?.let { bitmap ->
                    // Simpan gambar dari currentImageBitmap ke file (contoh: internal storage)
                    val imageFile = saveBitmapToFile(bitmap)
                    // Selanjutnya, gunakan imageFile untuk mengunggah gambar
                    imageFile
                }

                viewModel.getSession().observe(this) { setting ->
                    if (setting.token.isNotEmpty()) {
                        val imgPart = MultipartBody.Part.createFormData("photo", imageFile?.name ?: "default_filename", RequestBody.create("image/*".toMediaTypeOrNull(),
                            imageFile!!
                        ))
                        viewModel.addStory(setting.token, imgPart, requestBody)
                    }
                    messageToast(getString(R.string.storry_added))
                }
            }
        }

    }

    private fun messageToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val fileName = "dummy.jpg"
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Menampilkan pesan kepada pengguna mengapa izin kamera diperlukan
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

            }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequest) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin kamera telah diberikan
                dispatchTakePictureIntent()
            } else {
                // Izin kamera ditolak oleh pengguna
            }
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

    private fun showImage() {
        currentImageUri.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgPreview.setImageURI(it)
        }

    }

    private fun load(result: Boolean) {
        if (result) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}