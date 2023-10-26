package com.example.waystoryapp.view.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.waystoryapp.databinding.ActivitySignUpBinding
import com.example.waystoryapp.view.login.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]


        viewModel.isLoading.observe(this) {
            loading(it)
        }

        binding.signupButton.setOnClickListener {
            val name = binding.namaEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()
            Log.i("SignupActivity", "onCreate: $name $email $pass")
            if (email.isEmpty() && pass.isBlank() && email.isBlank() || pass.isEmpty() || name.isBlank() || name.isEmpty()) {
                messageToast("Tidak Boleh Kosong")
            } else {
                viewModel.signUp(name, email, pass)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }

    private fun messageToast(str: String){
        Toast.makeText(this,str, Toast.LENGTH_SHORT).show()
    }

    private fun loading(result: Boolean?) {
        if (result == true) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}
