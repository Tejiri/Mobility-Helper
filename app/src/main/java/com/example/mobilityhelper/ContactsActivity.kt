package com.example.mobilityhelper

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilityhelper.adapters.ContactAdapter
import com.example.mobilityhelper.databinding.ActivityContactsBinding
import com.example.mobilityhelper.databinding.ActivityHomeBinding
import com.example.mobilityhelper.models.Contact

class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding
    var contactArray = ArrayList<Contact>()
    val adapter = ContactAdapter(contactArray)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvContacts.layoutManager = LinearLayoutManager(this)
        getContacts()

        binding.etContactSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var newContactArray = ArrayList<Contact>()
                for (i in 0 until contactArray.size) {
                    var contact = contactArray[i]
                    if (contact.displayName.toLowerCase().contains(p0.toString().toLowerCase())) {
                        newContactArray.add(contact)
                    }
                }
                binding.rvContacts.adapter = ContactAdapter(newContactArray)
//                adapter.updateContactArray(newContactArray)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        println("Request code - $requestCode")

        if (permissions.contains("android.permission.READ_CONTACTS")) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts()
            }
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getContacts() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ContactsActivity, arrayOf(Manifest.permission.READ_CONTACTS), 1
            )
        } else {

            val phones = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            while (phones!!.moveToNext()) {
                val name =
                    phones.getString(with(phones) { getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) })
                val phoneNumber =
                    phones.getString(with(phones) { getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) })

                val id =
                    phones.getString(with(phones) { getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID) })

                val contact = Contact(name, phoneNumber)
                println(contact.number)
                contactArray.add(contact)
            }

            binding.rvContacts.adapter = adapter
            phones.close()
        }
    }
}