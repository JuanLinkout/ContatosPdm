package com.example.myapplication.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.databinding.TileContactBinding
import com.example.myapplication.model.Contact

class ContactAdapter(
    context: Context,
    private val contactList: MutableList<Contact>
) : ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact = contactList[position]
        var binding: TileContactBinding? = null

        var contactTileView = convertView
        if (contactTileView == null) {
            binding = TileContactBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            contactTileView = binding.root

            val tileContactHolder = TileContactHolder(binding.nameTv, binding.emailTv)
            contactTileView.tag = tileContactHolder
        }

        val holder = contactTileView.tag as TileContactHolder
        holder.nameTv.setText(contact.name)
        holder.emailTv.setText(contact.email)

        return contactTileView
    }

    private data class TileContactHolder(val nameTv: TextView, val emailTv: TextView)

}