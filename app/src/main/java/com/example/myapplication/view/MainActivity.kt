package com.example.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.R
import com.example.myapplication.adapter.ContactAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.Constant.EXTRA_CONTACT
import com.example.myapplication.model.Contact

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    //Adapter
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(
            this,
            contactList
        )
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)

        fillContacts()
        binding.contatoLv.adapter = contactAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        { result ->
            if(result.resultCode == RESULT_OK) {
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                contact?.let{_contact ->
                    if (contactList.any { it.id == contact.id }) {
                        val position = contactList.indexOfFirst { it.id == contact.id }
                        contactList[position] = _contact
                    }
                    else {
                        contactList.add(_contact)
                    }
                    contactAdapter.notifyDataSetChanged()
                }
            }

        }

        registerForContextMenu(binding.contatoLv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        return when (item.itemId){
            R.id.removeContactMI -> {
                contactList.removeAt(position)
                contactAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Contact Removed.", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editContactMI -> {
                val contact = contactList[position]
                val editContactIntent = Intent(this, ContactActivity::class.java)
                editContactIntent.putExtra(EXTRA_CONTACT, contact)
                carl.launch(editContactIntent)
                true
            }
            else -> { false }
        }
    }

    private fun fillContacts() {
        for (i in 1..50){
            contactList.add(
                Contact(
                    i,
                    "Nome $i",
                    "Endereço $i",
                    "Telefone $i",
                    "Email $i"
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(binding.contatoLv)
    }
}