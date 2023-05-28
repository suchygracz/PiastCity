package eventCreation

import android.app.Activity
import com.google.firebase.Timestamp
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.piastcity.MyLocationDemoActivity
import com.example.piastcity.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import event.Event
import java.io.File
import java.util.Calendar
import java.util.Date


class EventCreator : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private var eventName = ""
    private lateinit var editTextStartDate: EditText
    private var startYear = 0
    private var startMonth = 0
    private var startDay = 0
    private lateinit var editTextStartTime: EditText
    private var startHour = 0
    private var startMin = 0
    private lateinit var editTextEndDate: EditText
    private var endYear = 0
    private var endMonth = 0
    private var endDay = 0
    private lateinit var editTextEndTime: EditText
    private var endHour = 0
    private var endMin = 0
    private lateinit var btnStartDate: Button
    private lateinit var btnStartTime: Button
    private lateinit var btnEndDate: Button
    private lateinit var btnEndTime: Button
    private lateinit var btnTakePic: Button

    private lateinit var btnSave: Button
    private lateinit var checkBoxIsBooze: CheckBox
    private var isBooze = false
    private lateinit var checkBoxIsOutdoor: CheckBox
    private var isOutdoor  = false
    private lateinit var checkBoxIsPublic: CheckBox
    private var isPublic  = false

    private val REQUEST_CODE = 42
    private val REQUEST_CODE_KIKUS = 69
    public val REQUEST_RESULT = "REQUEST_RESULT"
    private lateinit var photoFile: File
    private lateinit var btnSetLocation: Button
    private var longitude = 0.0
    private var latitude = 0.0
    // TODO - Czekamy aż wiktor załata logowanie
    // val owner = FirebaseAuth.getInstance().currentUser!!.displayName
//    private val owner = FirebaseAuth.getInstance().currentUser!!.uid
    private val email = FirebaseAuth.getInstance().currentUser!!.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_creator)
        initElements()
        setActions()
    }

    private fun initElements(){
        editTextName = findViewById(R.id.editEventName)
        editTextStartDate = findViewById(R.id.in_startDate)
        editTextStartTime = findViewById(R.id.in_startTime)
        editTextEndDate = findViewById(R.id.in_endDate)
        editTextEndTime = findViewById(R.id.in_endTime)
        btnStartDate = findViewById(R.id.btn_startDate)
        btnStartTime = findViewById(R.id.btn_startTime)
        btnEndDate = findViewById(R.id.btn_endDate)
        btnEndTime = findViewById(R.id.btn_endTime)
        btnSave = findViewById(R.id.btn_save)
        checkBoxIsBooze = findViewById(R.id.checkbox_isBooze)
        checkBoxIsOutdoor = findViewById(R.id.checkbox_isOutdoor)
        btnTakePic = findViewById(R.id.btn_takePic)
        checkBoxIsPublic = findViewById(R.id.checkbox_isPublic)
        btnSetLocation = findViewById(R.id.btn_setLocalization)
    }

    private fun setActions(){
        btnStartDate.setOnClickListener { buttonStartDate() }
        btnStartTime.setOnClickListener { buttonStartTime() }
        btnEndDate.setOnClickListener { buttonEndDate() }
        btnEndTime.setOnClickListener { buttonEndTime() }
        btnSave.setOnClickListener { buttonSave() }
        btnTakePic.setOnClickListener { buttonTakePic() }
        btnSetLocation.setOnClickListener { buttonSetLocalization() }
    }

    private fun buttonTakePic(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile()
        val fileProvider = FileProvider.getUriForFile(this, "eventCreation.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        startActivityForResult(takePictureIntent, REQUEST_CODE)

        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("images/$email.jpg")
        imagesRef.putFile(photoFile.toUri())
    }

    private fun getPhotoFile(): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(email, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val storageRef = FirebaseStorage.getInstance().reference
            val imagesRef = storageRef.child("images/$email.jpg")
            imagesRef.putFile(photoFile.toUri())
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)

        }

        if(requestCode == REQUEST_CODE_KIKUS && resultCode == Activity.RESULT_OK){
//            val storageRef = FirebaseStorage.getInstance().reference
//            val imagesRef = storageRef.child("images/$owner.jpg")
//            imagesRef.putFile(photoFile.toUri())
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun buttonStartDate(){
        val c = Calendar.getInstance()
        startYear = c.get(Calendar.YEAR)
        startMonth = c.get(Calendar.MONTH)
        startDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->

            var month = ""
            if(monthOfYear<9)
                month+="0"
            month+=monthOfYear+1
            var day=""
            if(dayOfMonth<10)
                day+="0"
            day+=dayOfMonth
            // Display Selected date in textbox
            editTextStartDate.setText("$day-$month-$year")

        }, startYear, startMonth, startDay)
        datePickerDialog.show()
    }

    private fun buttonStartTime(){
        val c = Calendar.getInstance()
        startHour = c.get(Calendar.HOUR)
        startMin = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->

            var viewHour = ""
            if(hour<10)
                viewHour+="0"
            viewHour+=hour

            var viewMinute=""
            if(minute<10)
                viewMinute+="0"
            viewMinute+= minute

            editTextStartTime.setText("$viewHour:$viewMinute")
        }, startHour, startMin, true)
        timePickerDialog.show()
    }

    private fun buttonEndDate(){
        val c = Calendar.getInstance()
        endYear = c.get(Calendar.YEAR)
        endMonth = c.get(Calendar.MONTH)
        endDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->

            var month = ""
            if(monthOfYear<9)
                month+="0"
            month+=monthOfYear+1
            var day=""
            if(dayOfMonth<10)
                day+="0"
            day+=dayOfMonth
            // Display Selected date in textbox
            editTextEndDate.setText("$day-$month-$year")

        }, endYear, endMonth, endDay)
        datePickerDialog.show()
    }

    private fun buttonEndTime(){
        val c = Calendar.getInstance()
        endHour = c.get(Calendar.HOUR)
        endMin = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->

            var viewHour = ""
            if(hour<10)
                viewHour+="0"
            viewHour+=hour

            var viewMinute=""
            if(minute<10)
                viewMinute+="0"
            viewMinute+= minute

            editTextEndTime.setText("$viewHour:$viewMinute")
        }, endHour, endMin, true)
        timePickerDialog.show()
    }


    private fun buttonSetLocalization(){
        val mapsIntent = Intent(this, MyLocationDemoActivity::class.java)
//        startActivity(mapsIntent)
//        getContent.launch("string/*")
//        var cos = intent.data
//        Log.d("gownokurwa", cos.toString())
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
    }


    private fun getData(){
        eventName = editTextName.text.toString()
        val arrStartDate = editTextStartDate.text.toString().split("-")
        startDay = Integer.parseInt(arrStartDate[0])
        startMonth = Integer.parseInt(arrStartDate[1])
        startYear = Integer.parseInt(arrStartDate[2])
        val arrStartTime = editTextStartTime.text.toString().split(":")
        startHour = Integer.parseInt(arrStartTime[0])
        startMin = Integer.parseInt(arrStartTime[1])
        val arrEndDate = editTextEndDate.text.toString().split("-")
        endDay = Integer.parseInt(arrEndDate[0])
        endMonth = Integer.parseInt(arrEndDate[1])
        endYear = Integer.parseInt(arrEndDate[2])
        val arrEndTime = editTextEndTime.text.toString().split(":")
        endHour = Integer.parseInt(arrEndTime[0])
        endMin = Integer.parseInt(arrEndTime[1])

        if(checkBoxIsBooze.isChecked)
            isBooze=true
        if(checkBoxIsOutdoor.isChecked)
            isOutdoor=true
        if(checkBoxIsPublic.isChecked)
            isPublic=true

        //longtitude i latitude pobrane z api z mapy
    }

    private fun sendEvent() {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("images/$email.jpg")
        imagesRef.downloadUrl.addOnSuccessListener {
            val startDate = Date(startYear-1900, startMonth-1, startDay, startHour, startMin)
            val startTS = Timestamp(startDate)
            val endDate = Date(endYear-1900,endMonth-1,endDay,endHour,endMin)
            val endTS = Timestamp(endDate)
            val event = Event(
                eventName,
                email,
                isOutdoor,
                isBooze,
                isPublic,
                longitude, //x pobrane z mapy
                latitude, //y pobrane z mapy
                Timestamp.now(),
                startTS,
                endTS,
                it.toString()
            )
            Firebase.firestore.collection("events").add(event)
                .addOnFailureListener{
                Toast.makeText(
                    this,
                    "Fail while adding event to database!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun buttonSave(){
        getData()
        sendEvent()
        finish()
    }
}