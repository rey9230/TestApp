package cu.nico.infinity.testapp.network

import cu.nico.infinity.testapp.model.CommentsModel
import cu.nico.infinity.testapp.model.PostModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("posts")
    fun fetchAllPosts(): Call<List<PostModel>>

    @GET("comments")
    fun getCommentsByPost(@Query("postId") postId : Int) : Call<List<CommentsModel>>

}
