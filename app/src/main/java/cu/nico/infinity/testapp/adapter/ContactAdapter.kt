package cu.nico.infinity.testapp.adapter

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cu.nico.infinity.testapp.R
import cu.nico.infinity.testapp.model.Contact


/**
 * Created by Govind on 05/21/2018.
 */
class ContactAdapter(private val contactList: ArrayList<Contact>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]

        holder.bindItems(contact)
        holder.itemView.findViewById<ImageButton>(R.id.ibCall).setOnClickListener {

            val context = holder.itemView.context
            showDialog(contact.id, context, contact.name,contact.number, contact.rawContactId.toString(), position)
        }
        /*holder.itemView.findViewById<ImageButton>(R.id.ibMessage).setOnClickListener {
            if (onCallListener != null) {
                onCallListener!!.onMessage(contact)
            }
        }*/
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        fun bindItems(contact: Contact) {
            val tvName = itemView.findViewById<TextView>(R.id.tvName)
            val tvNumber = itemView.findViewById<TextView>(R.id.tvNumber)
            tvName.text = contact.name
            tvNumber.text = contact.number

        }
    }

    private fun showDialog(
        id: String,
        context: Context,
        name: String,
        phone: String,
        rawContactId: String,
        position: Int
    ) {

        val inflate_view = LayoutInflater.from(context).inflate(R.layout.transferencia_card, null)
        val nameet = inflate_view.findViewById(R.id.nameet) as EditText
        val phone_numberet = inflate_view.findViewById(R.id.phone_numberet) as EditText
        val updatebtn = inflate_view.findViewById(R.id.updatebtn) as CardView
        val deletebtn = inflate_view.findViewById(R.id.deletebtn) as CardView

        nameet.setText(name)
        phone_numberet.setText(phone)

        val editedname = nameet.text
        val editedphone = phone_numberet.text
        val alertDialog = android.app.AlertDialog.Builder(context)
        alertDialog.setView(inflate_view)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()


        updatebtn.setOnClickListener() {

            val contentResolver: ContentResolver = context.contentResolver

            val where = (ContactsContract.Data.RAW_CONTACT_ID + " = ? AND "
                    + ContactsContract.Data.MIMETYPE + " = ?")

            val nameParams = arrayOf(
                rawContactId,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            val numberParams = arrayOf(
                rawContactId,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            )

            val ops: ArrayList<ContentProviderOperation> = ArrayList()

            if (editedname.toString().isNotEmpty()) {
                ops.add(
                    ContentProviderOperation.newUpdate(
                        ContactsContract.Data.CONTENT_URI
                    )
                        .withSelection(where, nameParams)
                        .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            editedname.toString()
                        ).build()
                )
            }
            if (editedphone.toString().isNotEmpty()) {
                ops.add(
                    ContentProviderOperation.newUpdate(
                        ContactsContract.Data.CONTENT_URI
                    )
                        .withSelection(where, numberParams)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, editedphone.toString())
                        .build()
                )
            }
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)

            Toast.makeText(context,"Contact Information Updated", Toast.LENGTH_LONG).show()
            this.notifyItemChanged(position)
            this.notifyItemRangeChanged(position, contactList.size)
            this.notifyDataSetChanged()
            dialog.dismiss()

        }

        deletebtn.setOnClickListener() {

            deleteContactById(id,context)
            contactList.removeAt(position)


            this.notifyItemRemoved(position)
            this.notifyItemRangeChanged(position, contactList.size)
            this.notifyDataSetChanged()
            Toast.makeText(context,"Contact Information Deleted", Toast.LENGTH_LONG).show()
            dialog.dismiss()

        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

    }
    private fun deleteContactById(id: String, context: Context) {
        val cr = context.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null)
        cur?.let {
            try {
                if (it.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup._ID)) == id) {
                            val lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                            val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
                            cr.delete(uri, null, null)
                            break
                        }

                    } while (it.moveToNext())
                }

            } catch (e: Exception) {
                println(e.stackTrace)
            } finally {
                it.close()
                cur.close()
            }
        }
    }
}
