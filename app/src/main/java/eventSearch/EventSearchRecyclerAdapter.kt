package eventSearch

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.piastcity.R
import event.Event as PartyEvent

class EventSearchRecyclerAdapter(
    private val events: ArrayList<PartyEvent>,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<EventSearchRecyclerAdapter.EventSearchViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSearchViewHolder {
        return EventSearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.event_cell, parent, false),
            onItemListener,
            events
        )
    }

    override fun getItemCount(): Int {
        return events.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EventSearchViewHolder, position: Int) {
        val event = events[position]
        var type = "Indoor"
        if(event.isOutdoor == true)
            type = "Outdoor"
        holder.eventName.text = "Name: " + event.name
       //holder.eventAddress.text = "Address: " + event.address jakos ogarnac wspolprace ze wspolrzednymi
        holder.eventOwner.text = "Owner: " + event.owner
        holder.eventType.text = "Type: " + type
    }

    interface OnItemListener{
        fun onItemClick(position: Int, event: PartyEvent)
    }

    class EventSearchViewHolder(
        itemView: View,
        onItemListener: OnItemListener,
        events: ArrayList<PartyEvent>
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val onItemListener: OnItemListener
        private val events: ArrayList<PartyEvent>
        val eventName: TextView
        val eventOwner: TextView
        val eventAddress: TextView
        val eventType: TextView
        init {
            this.events = events
            this.onItemListener = onItemListener
            itemView.setOnClickListener(this)

            //visible content
            eventName = itemView.findViewById(R.id.eventName)
            eventOwner = itemView.findViewById(R.id.eventOwner)
            eventAddress = itemView.findViewById(R.id.eventAddress)
            eventType = itemView.findViewById(R.id.eventType)
        }

        override fun onClick(p0: View?) {
            onItemListener.onItemClick(adapterPosition, events[adapterPosition])
        }

    }
}