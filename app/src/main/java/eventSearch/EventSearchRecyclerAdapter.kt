package eventSearch

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import event.Event as PartyEvent

class EventSearchRecyclerAdapter(
    private val events: ArrayList<PartyEvent?>,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<EventSearchRecyclerAdapter.EventSearchViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSearchViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: EventSearchViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    interface OnItemListener{
        fun onItemClick(position: Int)
    }

    class EventSearchViewHolder(
        itemView: View,
        onItemListener: OnItemListener,
        events: ArrayList<PartyEvent?>
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val onItemListener: OnItemListener
        private val events: ArrayList<PartyEvent?>
        val parentView: View
        val eventName: TextView
        init {
            this.events = events
        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }
}