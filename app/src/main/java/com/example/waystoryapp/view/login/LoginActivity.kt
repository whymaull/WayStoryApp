package com.example.waystoryapp.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.waystoryapp.R
import com.example.waystoryapp.databinding.ActivityLoginBinding
import com.example.waystoryapp.view.ViewModelFactory
import com.example.waystoryapp.view.main.MainActivity
import com.example.waystoryapp.view.signup.SignUpActivity


class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this){
            load(it)
        }
        initAction()
    }

    private fun initAction() {
        binding.tvToRegister.setOnClickListener {
            SignUpActivity.start(this)
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()
            when {
                email.isBlank() -> {
                    binding.emailEditText.requestFocus()
                    binding.emailEditText.error = getString(R.string.error_empty_email)
                }
                !email.isEmailValid() -> {
                    binding.emailEditText.requestFocus()
                    binding.emailEditText.error = getString(R.string.error_invalid_email)
                }
                pass.isBlank() -> {
                    binding.passwordEditText.requestFocus()
                    binding.passwordEditText.error = getString(R.string.error_empty_password)
                }
                else ->{
                    viewModel.signIn(email, pass)
                    viewModel.isSuccess.observe(this) { isSuccess ->
                        if (isSuccess) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                    }
                }
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

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

}
