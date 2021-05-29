package cu.nico.infinity.testapp.ui

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cu.nico.infinity.testapp.R
import cu.nico.infinity.testapp.adapter.NewsAdapter
import cu.nico.infinity.testapp.model.PostModel
import cu.nico.infinity.testapp.viewmodels.HomeViewModel


class NewsFragment : Fragment() {
    private lateinit var vm: HomeViewModel
    private lateinit var adapter: NewsAdapter

    private lateinit var rv_home: RecyclerView
    private lateinit var progress_home: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm = ViewModelProvider(this)[HomeViewModel::class.java]
        rv_home = view.findViewById(R.id.rv_home);
        progress_home = view.findViewById(R.id.progress_home);
        initAdapter()

        vm.fetchAllPosts()

        vm.postModelListLiveData?.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                rv_home.visibility = View.VISIBLE
                adapter.setData(it as ArrayList<PostModel>)
            }else{
                showToast("Something went wrong")
            }
            progress_home.visibility = View.GONE
        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initAdapter() {
        adapter = NewsAdapter(context)
        rv_home.layoutManager = LinearLayoutManager(context)
        rv_home.adapter = adapter
    }


    private fun showToast(msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}