package cu.nico.infinity.testapp.listener

interface OnCallListener<T> {

    fun onCall(t: T)

    fun onMessage(t: T)
}
