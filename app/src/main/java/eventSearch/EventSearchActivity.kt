package eventSearch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.piastcity.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import event.Event
import eventCreation.EventCreator
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout
import event.Event as PartyEvent

class EventSearchActivity : AppCompatActivity(), EventSearchRecyclerAdapter.OnItemListener {
    private val database = Firebase.firestore
    private lateinit var addEventButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventList: ArrayList<PartyEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_search)
        // attach RecyclerView
        recyclerView = findViewById(R.id.eventRecyclerView)
        // set custom recycler layout from github
        setRecycler()
        // set button
        setAddEventOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        setSearchView()
    }

    private fun setRecycler() {
        val linearLayoutManager = ZoomRecyclerLayout(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.isNestedScrollingEnabled = false

    }

    private fun setSearchView() {
        eventList = ArrayList()
        database.collection("events").get().addOnSuccessListener {
            if(it.isEmpty == false) {
                for (data in it.documents) {
                    val event = data.toObject<PartyEvent>()
                    eventList.add(event!!)
                }
            }
            recyclerView.adapter = EventSearchRecyclerAdapter(eventList, this)
        }
    }

    private fun setAddEventOnClickListener() {
        addEventButton = findViewById(R.id.addEventButton)
        addEventButton.setOnClickListener {
            val goToEventCreator = Intent(this, EventCreator::class.java)
            startActivity(goToEventCreator)
        }
    }

    override fun onItemClick(position: Int, event: Event) {
        // open the event activity 
    }
}