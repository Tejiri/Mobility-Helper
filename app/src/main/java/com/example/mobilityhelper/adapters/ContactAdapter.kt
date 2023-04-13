package com.example.mobilityhelper.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilityhelper.R
import com.example.mobilityhelper.databinding.RvContactsBinding
import com.example.mobilityhelper.databinding.RvDriversBinding
import com.example.mobilityhelper.models.Contact

class ContactAdapter(private var contacts: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    private lateinit var binding: RvContactsBinding

    inner class ViewHolder(binding: RvContactsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivContactPhone.setOnClickListener {
                var position: Int = adapterPosition
                var contact: Contact = contacts[position]

                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:" + contact.number)
                itemView.context.startActivity(callIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
        binding = RvContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ContactAdapter.ViewHolder, position: Int) {
        var contact: Contact = contacts[position]

        if (contact.displayName.length > 15) {
            contact.displayName = contact.displayName.substring(0, 14) + "..."
        }
        binding.tvContactName.text = contact.displayName
        binding.tvContactNumber.text = contact.number
    }

    override fun getItemCount() = contacts.size

    fun updateContactArray(newContacts:ArrayList<Contact>){
        contacts = newContacts
        notifyDataSetChanged()
    }
}