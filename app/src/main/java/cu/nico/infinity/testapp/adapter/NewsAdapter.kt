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
import cu.nico.infinity.testapp.model.PostModel
import cu.nico.infinity.testapp.ui.CommentsActivity

class NewsAdapter(var listener: Context?) : RecyclerView.Adapter<NewsAdapter.HomeViewHolder>(){

    private var data : ArrayList<PostModel>?=null
    //private lateinit var cardView: CardView
    //private lateinit var tv_home_item_body: TextView

    interface HomeListener{
        fun onItemDeleted(postModel: PostModel, position: Int)
    }

    fun setData(list: ArrayList<PostModel>){
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.posts_item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bindView(item)
        holder.itemView.findViewById<CardView>(R.id.cardview).setOnClickListener {
            val context = holder.itemView.context

            val showCommentsActivity = Intent(context, CommentsActivity::class.java)
            showCommentsActivity.putExtra("postId", item?.id)
            showCommentsActivity.putExtra("title", item?.title)
            showCommentsActivity.putExtra("body", item?.body)
            context.startActivity(showCommentsActivity)

        }
    }

    fun addData(postModel: PostModel) {
        data?.add(0,postModel)
        notifyItemInserted(0)
    }

    fun removeData(position: Int) {
        data?.removeAt(position)
        notifyDataSetChanged()
    }

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val tv_home_item_title: TextView = itemView.findViewById(R.id.tv_home_item_title)
        private val tv_home_item_body: TextView = itemView.findViewById(R.id.tv_home_item_body)

        fun bindView(item: PostModel?) {
            tv_home_item_title.text = item?.title
            tv_home_item_body.text = item?.body
        }

    }

}
