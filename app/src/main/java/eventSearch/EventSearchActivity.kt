package eventSearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piastcity.R
import event.Event
import event.EventType
import java.time.LocalDateTime
import event.Event as PartyEvent

class EventSearchActivity : AppCompatActivity(), EventSearchRecyclerAdapter.OnItemListener {
    private lateinit var recyclerView: RecyclerView
//    private lateinit var recyclerAdapter: EventSearchRecyclerAdapter
    private lateinit var eventList: ArrayList<PartyEvent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_search)

        recyclerView = findViewById(R.id.eventRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setSearchView()
    }

    private fun setSearchView() {
        val party1 = PartyEvent("chuj",
            "dziekanowi",
            "naryj",
            EventType.Klub,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        val party2 = PartyEvent("chuj",
            "dziekanowi",
            "naryj",
            EventType.Klub,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        val party3 = PartyEvent("chuj",
            "dziekanowi",
            "naryj",
            EventType.Klub,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        eventList = ArrayList()
        eventList.add(party1)
        eventList.add(party2)
        eventList.add(party3)
        recyclerView.adapter = EventSearchRecyclerAdapter(eventList, this)
    }

    override fun onItemClick(position: Int, event: Event) {
        // open the event activity 
    }
}