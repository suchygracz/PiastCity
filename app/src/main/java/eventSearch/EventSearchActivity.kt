package eventSearch

import android.content.Intent
import android.os.Bundle
import android.view.OrientationEventListener
import android.widget.Toast
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
    private var orientationEventListener: OrientationEventListener? = null
    // TODO - Czekamy aż wiktor załata logowanie
    // val owner = FirebaseAuth.getInstance().currentUser!!.displayName
    private val owner = "fake"

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
        orientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                if (orientation in 45..134 || orientation in 225..314) {
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                } else {
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                }
            }
        }

        orientationEventListener?.enable()
    }

    override fun onPause() {
        super.onPause()

        orientationEventListener?.disable()
    }
//val linearLayoutManager = ZoomRecyclerLayout(this)
    private fun setRecycler() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerView)
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
        // like the event
    }

    override fun onItemLongClick(position: Int, event: Event) {
//        && removeImageFromDatabase(event)
//        if(removeEventFromDatabase(event)) {
            removeEventFromDatabase(event)
            eventList.removeAt(position)
            recyclerView.adapter!!.notifyItemRemoved(position)
        //            Toast.makeText(this, "Delete successfully!", Toast.LENGTH_LONG).show()
//        }
//        else {
//            Toast.makeText(this, "Delete failure!", Toast.LENGTH_SHORT).show()
//        }
    }

//    private fun removeImageFromDatabase(event: Event):Boolean {
//        //TODO - connect image with event
//    }

    private fun removeEventFromDatabase(event: Event):Boolean {
        var result = false
        database.collection("events")
            .whereEqualTo("name", event.name)
            .whereEqualTo("owner", owner)
            .whereEqualTo("creation", event.creation)
            .get().addOnSuccessListener {documents ->
                if(!documents.isEmpty) {
                    val documentID = documents.documents[0].id
                    database.collection("events")
                        .document(documentID)
                        .delete()
                        .addOnSuccessListener {
                            result = true
                        }
                }
            }
        return result
    }
}