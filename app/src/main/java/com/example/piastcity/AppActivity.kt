package com.example.piastcity
// klasa dla aplikacji
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }
}