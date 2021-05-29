package cu.nico.infinity.testapp.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cu.nico.infinity.testapp.R
import cu.nico.infinity.testapp.model.CommentsModel
import cu.nico.infinity.testapp.model.PostModel
import cu.nico.infinity.testapp.ui.CommentsActivity

class CommentsAdapter(var listener: Context?) : RecyclerView.Adapter<CommentsAdapter.HomeViewHolder>(){

    private var data : ArrayList<CommentsModel>?=null

    fun setData(list: ArrayList<CommentsModel>){
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comments_item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bindView(item)
        /*holder.itemView.findViewById<CardView>(R.id.cardview).setOnClickListener {
            val context = holder.itemView.context
        }*/
    }
    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val commentName: TextView = itemView.findViewById(R.id.commentName)
        private val commnentEmail: TextView = itemView.findViewById(R.id.commnentEmail)
        private val commnentBody: TextView = itemView.findViewById(R.id.commnentBody)

        fun bindView(item: CommentsModel?) {
            commentName.text = item?.name
            commnentEmail.text = item?.email
            commnentBody.text = item?.body
        }

    }

}
