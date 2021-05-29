package cu.nico.infinity.testapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cu.nico.infinity.testapp.model.CommentsModel
import cu.nico.infinity.testapp.model.PostModel
import cu.nico.infinity.testapp.network.ApiClient
import cu.nico.infinity.testapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {

    private var apiInterface: ApiInterface?=null

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
    }

    fun fetchAllPosts(): MutableLiveData<List<PostModel>?> {
        val data = MutableLiveData<List<PostModel>?>()
        apiInterface?.fetchAllPosts()?.enqueue(object : Callback<List<PostModel>>{
            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                data.value = null
            }
            override fun onResponse(
                call: Call<List<PostModel>>,
                response: Response<List<PostModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 &&  res!=null){
                    data.value = res
                }else{
                    data.value = null
                }
            }
        })
        return data
    }

    fun fetchPostComments(postId : Int): MutableLiveData<List<CommentsModel>?> {
        val data = MutableLiveData<List<CommentsModel>?>()
        apiInterface?.getCommentsByPost(postId)?.enqueue(object : Callback<List<CommentsModel>>{
            override fun onFailure(call: Call<List<CommentsModel>>, t: Throwable) {
                data.value = null
            }
            override fun onResponse(
                call: Call<List<CommentsModel>>,
                response: Response<List<CommentsModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 &&  res!=null){
                    data.value = res
                }else{
                    data.value = null
                }
            }
        })
        return data
    }
}
