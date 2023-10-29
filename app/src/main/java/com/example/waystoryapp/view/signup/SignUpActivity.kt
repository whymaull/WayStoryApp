package com.example.waystoryapp.view.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.waystoryapp.databinding.ActivitySignUpBinding
import com.example.waystoryapp.view.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        viewModel.isLoading.observe(this) {
            load(it)
        }
        initAction()
    }

    private fun initAction() {
        binding.tvToLogin.setOnClickListener {
            LoginActivity.start(this)
        }

        binding.signupButton.setOnClickListener {
            val name = binding.namaEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()
            Log.i("SignupActivity", "onCreate: $name $email $pass")
            if(email.isEmpty() && pass.isBlank() && email.isBlank() || pass.isEmpty()  || name.isBlank() || name.isEmpty()  ){
                toast("Tidak Boleh Kosong")
            }else{
                viewModel.registerUser(name,email,pass)
                Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun load(result: Boolean) {
        if (result) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }
}

private fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this)
}
