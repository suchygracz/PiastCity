package com.example.piastcity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.example.piastcity.PermissionUtils
import com.example.piastcity.PermissionUtils.requestLocationPermissions

class UI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui)
        val user = Firebase.auth.currentUser.toString()
        Toast.makeText(this, user, Toast.LENGTH_LONG ).show()
    }

    fun goToMaps(view: View) {
        val mapsIntent = Intent(this, MyLocationDemoActivity::class.java)
        startActivity(mapsIntent)
//        startActivityForResult(intent, 42)
//        var cos = intent.data
//        Log.d("gownokurwa", cos.toString())
    }


}