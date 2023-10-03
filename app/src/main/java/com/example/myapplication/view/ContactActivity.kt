package com.example.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityContactBinding
import com.example.myapplication.model.Constant.EXTRA_CONTACT
import com.example.myapplication.model.Contact
import java.util.Random

class ContactActivity : AppCompatActivity() {

    private val binding: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Contact Details"

        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        receivedContact?.let { _receivedContact ->
            with(binding) {
                nameEt.setText(_receivedContact.name)
                addressEt.setText(_receivedContact.address)
                phoneEt.setText(_receivedContact.phone)
                emailEt.setText(_receivedContact.email)
            }
        }

        with (binding) {
            saveBt.setOnClickListener {
                val contact = Contact(
                    id = receivedContact?.id?:generateId(),
                    name = nameEt.text.toString(),
                    address = addressEt.text.toString(),
                    phone = phoneEt.text.toString(),
                    email = emailEt.text.toString()
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_CONTACT, contact)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun generateId() = Random(System.currentTimeMillis()).nextInt()
}