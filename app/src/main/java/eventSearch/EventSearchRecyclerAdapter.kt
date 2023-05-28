package eventSearch

import User.User
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.piastcity.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import event.Event as PartyEvent

class EventSearchRecyclerAdapter(
    private val events: ArrayList<PartyEvent>,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<EventSearchRecyclerAdapter.EventSearchViewHolder>(){
    private val database = Firebase.firestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSearchViewHolder {
        return EventSearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_ui, parent, false),
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
        holder.eventName.text = "Name: ${event.name}"
       //holder.eventAddress.text = "Address: " + event.address jakos ogarnac wspolprace ze wspolrzednymi
        holder.eventOwner.text = "Owner: ${event.owner}"
        holder.eventType.text = "Type: $type"
        Picasso.get().load(event.imageUrl).error(R.mipmap.ic_launcher).into(holder.eventImage)
        database.collection("users").whereEqualTo("firebaseUser", event.owner).get().addOnSuccessListener {
            if(!it.isEmpty) {
                val user = it.documents[0].toObject<User>()
                Picasso.get().load(user!!.imageUrl).error(R.mipmap.ic_launcher).into(holder.userImage)
            }
        }
    }

    interface OnItemListener{
        fun onItemClick(position: Int, event: PartyEvent)
        fun onItemLongClick(position: Int, event: PartyEvent)
    }

    class EventSearchViewHolder(
        itemView: View,
        onItemListener: OnItemListener,
        events: ArrayList<PartyEvent>
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        private val onItemListener: OnItemListener
        private val events: ArrayList<PartyEvent>
        val eventName: TextView
        val eventOwner: TextView
        val eventAddress: TextView
        val eventType: TextView
        val eventImage: ImageView
        val userImage: ImageView
        init {
            this.events = events
            this.onItemListener = onItemListener
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)

            //visible content
            eventName = itemView.findViewById(R.id.eventNamee)
            eventOwner = itemView.findViewById(R.id.Uzytkownik)
            eventAddress = itemView.findViewById(R.id.Localization)
            eventType = itemView.findViewById(R.id.eventTypee)
            eventImage = itemView.findViewById(R.id.eventPhoto)
            userImage = itemView.findViewById(R.id.AvatarPhoto)
        }

        override fun onClick(p0: View?) {
            onItemListener.onItemClick(adapterPosition, events[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            onItemListener.onItemLongClick(adapterPosition, events[adapterPosition])
            return true
        }
    }
}