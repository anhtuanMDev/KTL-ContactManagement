package com.example.contact_manager.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.contact_manager.R
import com.example.contact_manager.model.Contact

class ListViewAdapter(private val context: Context, private var contacts: List<Contact>) : BaseAdapter() {

    override fun getCount(): Int {
        return contacts.size
    }

    override fun getItem(position: Int): Contact {
        return contacts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            // Inflate the custom layout for each item
            view = LayoutInflater.from(context).inflate(R.layout.list_contact_item, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        // Get the current contact and set its data in the TextViews
        val contact = getItem(position)
        holder.tvName.text = contact.name
        holder.tvEmail.text = contact.email
        holder.tvPhone.text = contact.phone

        return view
    }

    // Method to update the list of contacts
    fun updateContacts(newContacts: List<Contact>) {
        Log.d("ERROR UPDATE CONTACTS", "newContacts " + newContacts.size);
        this.contacts = newContacts
        this.notifyDataSetChanged()
    }

    // ViewHolder class to hold views for recycling
    private class ViewHolder(view: View) {
        val tvName: TextView = view.findViewById(R.id.item_title)
        val tvEmail: TextView = view.findViewById(R.id.item_email)
        val tvPhone: TextView = view.findViewById(R.id.item_phone)
    }
}
