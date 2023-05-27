package com.example.piastcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class UI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui)
    }

    fun goToMaps(view: View) {}
}