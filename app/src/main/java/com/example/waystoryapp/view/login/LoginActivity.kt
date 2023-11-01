package com.example.waystoryapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
        playAnim()
    }

    @SuppressLint("Recycle")
    private fun playAnim() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.welcomeLogin, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.loginDesc, View.ALPHA, 1f).setDuration(100)
        val pesan = ObjectAnimator.ofFloat(binding.tvMessage, View.ALPHA, 1f).setDuration(100)

        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val inputEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(100)

        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val inputPassword = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(100)

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val toRegister = ObjectAnimator.ofFloat(binding.layoutTextRegister, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(title, desc, pesan, email, inputEmail, password, inputPassword, login, toRegister)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, pesan, email, inputEmail, password, inputPassword, login, toRegister)
            start()
        }
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
                pass.length < 8 -> {
                    binding.passwordEditText.requestFocus()
                    binding.passwordEditText.error = getString(R.string.error_short_password)
                }
                else ->{
                    viewModel.signIn(email, pass)
                    viewModel.isMessage.observe(this) { isMessage ->
                        Log.i("test", "$isMessage")
                        if (isMessage == getString(R.string.berhasil)) {
                            messageToast(getString(R.string.berhasil_login))
                            viewModel.isMessageNUlL()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        }
                        if (isMessage == getString(R.string.user_not_found)) {
                            messageToast(getString(R.string.email_dan_password_tidak_terdaftar))
                            viewModel.isMessageNUlL()

                        }
                        if (isMessage == getString(R.string.invalid_password)) {
                            messageToast(getString(R.string.password_salah))
                            viewModel.isMessageNUlL()
                        }
                    }
                }
            }
        }
    }


    private fun messageToast(message: String) {
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
