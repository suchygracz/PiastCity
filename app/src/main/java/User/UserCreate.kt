package User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.piastcity.R
import com.example.piastcity.databinding.ActivityUserCreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UserCreate : AppCompatActivity() {

    private lateinit var binding: ActivityUserCreateBinding;
    private lateinit var firebaseAuth: FirebaseAuth;

    var username: String? = null
    var firebaseUser: String? = null
    private lateinit var userPhoto: File
    private val owner = "fake"
    private val REQUEST_CODE = 42
    var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_create)
        binding = ActivityUserCreateBinding.inflate(layoutInflater)

    }

    private fun getPhotoFile(): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(owner, ".jpg", storageDirectory)
    }

    private fun getData(){
        username = binding.userNameInput.text.toString();
        firebaseUser = firebaseAuth.currentUser.toString()
        userPhoto = getPhotoFile()

    }

    private fun sendEvent() {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("images/$owner.jpg")
        imagesRef.putFile(userPhoto.toUri())
        imagesRef.downloadUrl.addOnSuccessListener {
            val user = User(
                username ,
                firebaseUser,
                imageUrl
            )

            Firebase.firestore.collection("testUser").add(user)
        }
    }
    private fun buttonSave(){
        getData()
        sendEvent()
        finish()
    }

    fun takeUserPhoto(view: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        userPhoto = getPhotoFile()
        val fileProvider = FileProvider.getUriForFile(this, "eventCreation.fileprovider", userPhoto)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        startActivityForResult(takePictureIntent, REQUEST_CODE)
    }
}