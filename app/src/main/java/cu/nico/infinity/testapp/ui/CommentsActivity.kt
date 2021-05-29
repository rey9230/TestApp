package cu.nico.infinity.testapp.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import cu.nico.infinity.testapp.R
import cu.nico.infinity.testapp.adapter.CommentsAdapter
import cu.nico.infinity.testapp.adapter.NewsAdapter
import cu.nico.infinity.testapp.model.CommentsModel
import cu.nico.infinity.testapp.model.PostModel
import cu.nico.infinity.testapp.viewmodels.HomeViewModel
import kotlin.properties.Delegates

class CommentsActivity : AppCompatActivity() {
    private lateinit var vm: HomeViewModel
    private lateinit var adapter: CommentsAdapter

    private lateinit var rv_comments: RecyclerView
    private lateinit var progress_comments: ProgressBar

    private var id by Delegates.notNull<Int>()
    private lateinit var title : String
    private lateinit var body : String
    private lateinit var newtitle : TextView
    private lateinit var newbody : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window? = window
        if (window != null) {
            window.decorView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = getColor(R.color.colorPrimary)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.navigationBarColor = getColor(R.color.colorPrimary)
            }
        }
        setContentView(R.layout.activity_comments)
        newtitle = findViewById(R.id.newtitle)
        newbody = findViewById(R.id.newbody)

        id = intent?.getIntExtra("postId", 1)!!
        title = intent.getStringExtra("title").toString()
        body = intent.getStringExtra("body").toString()


        newtitle.text = title
        newbody.text = body

        vm = ViewModelProvider(this)[HomeViewModel::class.java]
        rv_comments = findViewById(R.id.rv_comments);
        progress_comments = findViewById(R.id.progress_comments)

        initAdapter()

        vm.fetchAllComments(id)

        vm.commentsModelListLiveData?.observe(this, Observer {
            if (it!=null){
                rv_comments.visibility = View.VISIBLE
                adapter.setData(it as ArrayList<CommentsModel>)
            }else{
                showToast("Something went wrong")
            }
            progress_comments.visibility = View.GONE
        })

    }
    private fun initAdapter() {
        adapter = CommentsAdapter(this)
        rv_comments.layoutManager = LinearLayoutManager(this)
        rv_comments.adapter = adapter
    }


    private fun showToast(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}
