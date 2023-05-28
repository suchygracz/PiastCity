package eventSearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piastcity.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import event.Event
import eventCreation.EventCreator
import event.Event as PartyEvent

class EventSearchActivity : AppCompatActivity(), EventSearchRecyclerAdapter.OnItemListener {
    private val database = Firebase.firestore
    private lateinit var addEventButton: FloatingActionButton
    private lateinit var refreshButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventList: ArrayList<PartyEvent>
    // TODO - Czekamy aż wiktor załata logowanie zalatane kurwa
    // val owner = FirebaseAuth.getInstance().currentUser!!.displayName
//    private val owner = FirebaseAuth.getInstance().currentUser!!.uid
    private val owner = "fryta"
    override fun onResume() {
        super.onResume()
        setSearchView()
        Toast.makeText(this, "Choj", Toast.LENGTH_LONG).show()
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
            removeImageFromStorage(event)
            eventList.removeAt(position)
            recyclerView.adapter!!.notifyItemRemoved(position)
    }

    private fun removeImageFromStorage(event: Event) {
        val storageRef = FirebaseStorage.getInstance().reference
        event.imageUrl?.let { storageRef.storage.getReferenceFromUrl(it).delete() }
        //return result
    }

    private fun removeEventFromDatabase(event: Event):Boolean {
        var result = false
        database.collection("events")
            .whereEqualTo("name", event.name)
            //.whereEqualTo("owner", owner)
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