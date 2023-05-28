package User

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.piastcity.R
import com.example.piastcity.databinding.ActivityUserCreateBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import eventSearch.EventSearchActivity
import java.io.File

class UserCreate : AppCompatActivity() {

    private lateinit var binding: ActivityUserCreateBinding;
    private var firebaseAuth = FirebaseAuth.getInstance().currentUser

    var username: String? = null
    var firebaseUser: String? = firebaseAuth?.email
    private lateinit var userPhoto: File
    //private val owner = "fake"
    private val REQUEST_CODE = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_create)
        binding = ActivityUserCreateBinding.inflate(layoutInflater)

    }

    private fun getPhotoFile(): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(firebaseUser, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode== REQUEST_CODE&&resultCode== Activity.RESULT_OK){
            val storageRef = FirebaseStorage.getInstance().reference
            val imagesRef = storageRef.child("users/$firebaseUser.jpg")
            imagesRef.putFile(userPhoto.toUri())
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getData(){
        //username = binding.userNameInput.text.toString();
        username = findViewById<EditText>(R.id.userNameInput).text.toString()
        firebaseUser = firebaseAuth?.email
    }

    private fun sendUser() {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("users/$firebaseUser.jpg")
        imagesRef.downloadUrl.addOnSuccessListener {
            val user = User(
                username,
                firebaseUser,
                it.toString()
            )
            Firebase.firestore.collection("users").add(user)
        }
    }

    fun takeUserPhoto(view: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        userPhoto = getPhotoFile()
        val fileProvider = FileProvider.getUriForFile(this, "eventCreation.fileprovider", userPhoto)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        startActivityForResult(takePictureIntent, REQUEST_CODE)
    }

    fun saveProfile(view: View) {
        getData()
        sendUser()
        val intentapp = Intent(this, EventSearchActivity::class.java)
        startActivity(intentapp)
        finish()
    }
}