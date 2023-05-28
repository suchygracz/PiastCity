package eventSearch

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import android.view.OrientationEventListener
=======
import android.widget.Button
>>>>>>> b153fdf (Add refresh button to recycler layout)
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piastcity.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import event.Event
import eventCreation.EventCreator
import event.Event as PartyEvent

class EventSearchActivity : AppCompatActivity(), EventSearchRecyclerAdapter.OnItemListener {
    private val database = Firebase.firestore
    private lateinit var addEventButton: FloatingActionButton
    private lateinit var refreshButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventList: ArrayList<PartyEvent>
    private var orientationEventListener: OrientationEventListener? = null
    // val owner = FirebaseAuth.getInstance().currentUser!!.displayName
//    private val owner = FirebaseAuth.getInstance().currentUser!!.uid
    private val owner = "fryta"
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_search)

        // attach RecyclerView
        recyclerView = findViewById(R.id.eventRecyclerView)

        setRecycler()
        setButtons()
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

    private fun setButtons() {
        addEventButton = findViewById(R.id.addEventButton)
        refreshButton = findViewById(R.id.refreshButton)
        addEventButton.setOnClickListener {
            val goToEventCreator = Intent(this, EventCreator::class.java)
            startActivity(goToEventCreator)
            setSearchView()
        }
        refreshButton.setOnClickListener{
            setSearchView()
        }
    }

    private fun setSearchView() {
        eventList = ArrayList()
        database.collection("events").orderBy("creation").get().addOnSuccessListener {
            if(!it.isEmpty) {
                for (data in it.documents) {
                    val event = data.toObject<PartyEvent>()
                    eventList.add(event!!)
                }
            }
            recyclerView.adapter = EventSearchRecyclerAdapter(eventList, this)
        }
    }

    override fun onItemClick(position: Int, event: Event) {
        // like the event
    }

    override fun onItemLongClick(position: Int, event: Event) {
            removeEventFromDatabase(event)
            eventList.removeAt(position)
            recyclerView.adapter!!.notifyItemRemoved(position)
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