package c

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cu.nico.infinity.testapp.utils.isPermissionGranted

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cu.nico.infinity.testapp.R
import cu.nico.infinity.testapp.adapter.ContactAdapter
import cu.nico.infinity.testapp.listener.OnCallListener
import cu.nico.infinity.testapp.listener.Utility
import cu.nico.infinity.testapp.model.Contact
import kotlinx.coroutines.*


class ContactsFragment : Fragment() , OnCallListener<Contact> {
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    private lateinit var recyclerView : RecyclerView
    private lateinit var progress_home : ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_contacts, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        progress_home = view.findViewById(R.id.progress_home)


        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_CONTACTS)
            != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.WRITE_CONTACTS)) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.WRITE_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.WRITE_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        }else {
            loadContacts()
        }
    }

    override fun onCall(t: Contact) {
        context?.let {
            Utility.makeCall(it, t.number)
        }
    }

    override fun onMessage(t: Contact) {

        context?.let {
            Utility.doMessage(it, t.number)
        }
    }
    private fun loadContacts() {
        var contacts: ArrayList<Contact>
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    contacts = getContacts()
                }
            } finally {
                progress_home.visibility = View.INVISIBLE

            }
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val adapter = ContactAdapter(contacts)
            recyclerView.adapter = adapter
            //adapter.setListener(this@ContactsFragment)

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                showToast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private fun getContacts(): ArrayList<Contact> {
        val contacts = ArrayList<Contact>()
        val cursor = context?.contentResolver?.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

        if (cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
                    val rawContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID))
                    if (phoneNumber > 0) {
                        val cursorPhone = context?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                        if (cursorPhone != null) {
                            if (cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext()) {
                                    val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    contacts.add(Contact(id, name, phoneNumValue, rawContactId))
                                }
                            }
                        }
                        cursorPhone?.close()
                    }
                }
            } else {
                showToast("No contacts available!")
            }
        }
        cursor?.close()
        return contacts
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}