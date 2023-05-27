package com.example.piastcity

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.firebase.auth.FirebaseUser

data class User(
    val username: String? = null,
    val firebaseUser: String? = null,
    var imageUrl: String? = null
)
