package cu.nico.infinity.testapp.model


data class CommentsModel (
    val postId: Int = 0,
    val id: Int = 0,
    val name: String? = null,
    val email: String? = null,
    val body: String? = null
)