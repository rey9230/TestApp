package cu.nico.infinity.testapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cu.nico.infinity.testapp.data.HomeRepository
import cu.nico.infinity.testapp.model.CommentsModel
import cu.nico.infinity.testapp.model.PostModel

class HomeViewModel(application: Application): AndroidViewModel(application){

    private var homeRepository: HomeRepository?=null
    var postModelListLiveData : MutableLiveData<List<PostModel>?>?
    var commentsModelListLiveData : MutableLiveData<List<CommentsModel>?>?

    init {
        homeRepository = HomeRepository()
        postModelListLiveData = MutableLiveData()
        commentsModelListLiveData = MutableLiveData()
    }

    fun fetchAllPosts(){
        postModelListLiveData = homeRepository?.fetchAllPosts()
    }

    fun fetchAllComments(postId: Int){
        commentsModelListLiveData = homeRepository?.fetchPostComments(postId)
    }


}