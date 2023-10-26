package com.example.waystoryapp.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.waystoryapp.R
import com.example.waystoryapp.view.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 3000 // 3 detik
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Handler untuk menunda pindah ke activity utama
        Handler().postDelayed({
            // Intent untuk pindah ke activity utama
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Menutup splash screen setelah pindah ke activity utama
            finish()
        }, splashTimeOut)
    }
}