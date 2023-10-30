package com.example.waystoryapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.waystoryapp.R
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
        playAnim()
    }

    @SuppressLint("Recycle")
    private fun playAnim() {
        ObjectAnimator.ofFloat(binding.ivSignUp, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.welcomeSignup, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.welcomeDesc, View.ALPHA, 1f).setDuration(100)

        val nama = ObjectAnimator.ofFloat(binding.tvNama, View.ALPHA, 1f).setDuration(100)
        val inputNama = ObjectAnimator.ofFloat(binding.etNama, View.ALPHA, 1f).setDuration(100)

        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val inputEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(100)

        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val inputPassword = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(100)

        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val toLogin = ObjectAnimator.ofFloat(binding.layoutTextRegister, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(title, desc, nama, inputNama, email, inputEmail, password, inputPassword, signup, toLogin)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, nama, inputNama, email, inputEmail, password, inputPassword, signup, toLogin)
            start()
        }
    }

    private fun initAction() {
        binding.tvToLogin.setOnClickListener {
            LoginActivity.start(this)
        }

        binding.signupButton.setOnClickListener {
            val name = binding.namaEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()

            when {
                name.isBlank() -> {
                    binding.namaEditText.requestFocus()
                    binding.namaEditText.error = getString(R.string.error_empty_name)
                }
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
                else -> {
                    viewModel.registerUser(name,email,pass)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

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
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }
}


