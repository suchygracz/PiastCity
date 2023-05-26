package eventCreation

import com.google.firebase.Timestamp
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.content.FileProvider
import com.example.piastcity.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import event.Event
import java.io.File
import java.util.Calendar
import java.util.Date
import kotlin.math.min


private const val REQUEST_CODE = 42
private const val FILE_NAME = "photo.jpg"
private lateinit var photoFile: File
class EventCreator : AppCompatActivity() {
    lateinit var editTextName: EditText
    var eventName = ""
    lateinit var editTextStartDate: EditText
    var startYear = 0
    var startMonth = 0
    var startDay = 0
    lateinit var editTextStartTime: EditText
    var startHour = 0
    var startMin = 0
    lateinit var editTextEndDate: EditText
    var endYear = 0
    var endMonth = 0
    var endDay = 0
    lateinit var editTextEndTime: EditText
    var endHour = 0
    var endMin = 0
    lateinit var btnStartDate: Button
    lateinit var btnStartTime: Button
    lateinit var btnEndDate: Button
    lateinit var btnEndTime: Button
    lateinit var btnTakePic: Button

    lateinit var btnSave: Button
    lateinit var cb_isBooze: CheckBox
    var isBooze = false
    lateinit var cb_isOutdoor: CheckBox
    var isOutdoor  = false
    lateinit var cb_isPublic: CheckBox
    var isPublic  = false

    lateinit var eventImage: Bitmap
    lateinit var btnSetLocation: Button
    var longtitude = 0.0
    var latitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_creator)
        initElements()
        setActions()

    }

    fun initElements(){
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
        cb_isBooze = findViewById(R.id.checkbox_isBooze)
        cb_isOutdoor = findViewById(R.id.checkbox_isOutdoor)
        btnTakePic = findViewById(R.id.btn_takePic)
        cb_isPublic = findViewById(R.id.checkbox_isPublic)
        btnSetLocation = findViewById(R.id.btn_setLocalization)
    }

    fun setActions(){
        btnStartDate.setOnClickListener { buttonStartDate() }
        btnStartTime.setOnClickListener { buttonStartTime() }
        btnEndDate.setOnClickListener { buttonEndDate() }
        btnEndTime.setOnClickListener { buttonEndTime() }
        btnSave.setOnClickListener { buttonSave() }
        btnTakePic.setOnClickListener { buttonTakePic() }
        btnSetLocation.setOnClickListener { buttonSetLocalization() }
    }

    fun buttonTakePic(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)
        val fileProvider = FileProvider.getUriForFile(this, "eventCreation.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)


        startActivityForResult(takePictureIntent, REQUEST_CODE)
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //Bitmapa ze zdjeciem
            eventImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            //przykladowo imageView.setImageBitmap(takenImage)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun buttonStartDate(){
        val c = Calendar.getInstance()
        startYear = c.get(Calendar.YEAR)
        startMonth = c.get(Calendar.MONTH)
        startDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth ->

            var month = ""
            if(monthOfYear<9)
                month+="0"
            month+=monthOfYear+1
            var day=""
            if(dayOfMonth<10)
                day+="0"
            day+=dayOfMonth
            // Display Selected date in textbox
            editTextStartDate.setText("" + day + "-" + month + "-" + year)

        }, startYear, startMonth, startDay)
        datePickerDialog.show()
    }

    fun buttonStartTime(){
        val c = Calendar.getInstance()
        startHour = c.get(Calendar.HOUR)
        startMin = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hour, minute ->

            var viewHour = ""
            if(hour<10)
                viewHour+="0"
            viewHour+=hour

            var viewMinute=""
            if(minute<10)
                viewMinute+="0"
            viewMinute+= minute

            editTextStartTime.setText(""+viewHour+":"+viewMinute)
        }, startHour, startMin, true)
        timePickerDialog.show()
    }

    fun buttonEndDate(){
        val c = Calendar.getInstance()
        endYear = c.get(Calendar.YEAR)
        endMonth = c.get(Calendar.MONTH)
        endDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth ->

            var month = ""
            if(monthOfYear<9)
                month+="0"
            month+=monthOfYear+1
            var day=""
            if(dayOfMonth<10)
                day+="0"
            day+=dayOfMonth
            // Display Selected date in textbox
            editTextEndDate.setText("" + day + "-" + month + "-" + year)

        }, endYear, endMonth, endDay)
        datePickerDialog.show()
    }

    fun buttonEndTime(){
        val c = Calendar.getInstance()
        endHour = c.get(Calendar.HOUR)
        endMin = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hour, minute ->

            var viewHour = ""
            if(hour<10)
                viewHour+="0"
            viewHour+=hour

            var viewMinute=""
            if(minute<10)
                viewMinute+="0"
            viewMinute+= minute

            editTextEndTime.setText(""+viewHour+":"+viewMinute)
        }, endHour, endMin, true)
        timePickerDialog.show()
    }

    fun buttonSetLocalization(){
        //funkcja odpala api z mapa i zapisuje jego wynik w longtitude i latitude
    }

    fun getData(){
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

        if(cb_isBooze.isChecked)
            isBooze=true
        if(cb_isOutdoor.isChecked)
            isOutdoor=true
        if(cb_isPublic.isChecked)
            isPublic=true

        //longtitude i latitude pobrane z api z mapy
    }

    fun sendEvent(): Event{
        val startDate = Date(startYear, startMonth, startDay, startHour, startMin)
        val startTS = Timestamp(startDate)
        val endDate = Date(endYear,endMonth,endDay,endHour,endMin)
        val endTS = Timestamp(endDate)
        // TODO - Czekamy aż wiktor załata logowanie do bazy danych
//        val owner = FirebaseAuth.getInstance().currentUser!!.displayName
        val owner = "fake"
        val event = Event(
            eventName,
            owner,
            isOutdoor,
            isBooze,
            isPublic,
            longtitude, //x pobrane z mapy
            latitude, //y pobrane z mapy
            Timestamp.now(),
            startTS,
            endTS
        )
        Firebase.firestore.collection("events").add(event)
        return event
    }

    //mozna dodac ifa, ktory upewnia sie, ze sa wszystkie dane, ale po co
    fun buttonSave(){
        getData()
        sendEvent()
        //eventImage do firebase jako Bitmapa ze zdjeciem
        finish()
    }
}