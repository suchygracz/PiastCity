package eventSearch

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piastcity.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import event.Event
import event.EventType
import event.Event as PartyEvent

class EventSearchActivity : AppCompatActivity(), EventSearchRecyclerAdapter.OnItemListener {
    private val database = Firebase.firestore
    private lateinit var addEventButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventList: ArrayList<PartyEvent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_search)

        // set RecyclerView
        recyclerView = findViewById(R.id.eventRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setSearchView()

        // set button
//        setAddEventOnClickListener()
    }

//    private fun setAddEventOnClickListener() {
//        addEventButton = findViewById(R.id.addEventButton)
////        addEventButton.setOnClickListener()
//    }

    private fun setSearchView() {
        val party1 = PartyEvent(
            "test1",
            "test1",
            "test1",
//            EventType.Piknik,
//            false
        )
        val party2 = PartyEvent(
            "test2",
            "test2",
            "test2",
//            EventType.Klub,
//            true
        )
        val party3 = PartyEvent(
            "test3",
            "test3",
            "test3",
//            EventType.Plener,
//            true
        )

//        database.collection("events").document("t1").set(party1)
//        database.collection("events").document("t2").set(party2)
//        database.collection("events").document("t3").set(party3)
//        database.collection("events").add(party2)
//        database.collection("events").add(party3)

        eventList = ArrayList()
        database.collection("events").document("t1").get().addOnSuccessListener {documentsSpanshot ->
            eventList.add(documentsSpanshot.toObject<PartyEvent>()!!)
            val name = documentsSpanshot.toObject<PartyEvent>()!!.name
            val name2 = eventList[0].name
            if(name2 == "test1") {
                Toast.makeText(this,name2, Toast.LENGTH_LONG).show()
            }
        }
//        if(eventList.isEmpty()) {
//            Toast.makeText(this,"chuj", Toast.LENGTH_LONG).show()
//        }
        eventList.add(party1)

        recyclerView.adapter = EventSearchRecyclerAdapter(eventList, this)
    }

    override fun onItemClick(position: Int, event: Event) {
        // open the event activity 
    }
}