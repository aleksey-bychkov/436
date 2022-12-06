package com.example.myapplication

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ReportMessageBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class ReportMessage: AppCompatActivity() {

    // declare variables
    lateinit var message: String
    lateinit var messageID: String
    lateinit var reportedUID: String
    lateinit var reportingUID: String
    lateinit var binding: ReportMessageBinding
    lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
    lateinit var reason: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // intialize variables
        binding = ReportMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        // get relevant information from intent
        val extras = intent.extras
        if (extras != null) {
            message = extras.getString("message")!!
            messageID = extras.getString("messageID")!!
            reportedUID = extras.getString("reportedUID")!!
            reportingUID = extras.getString("reportingUID")!!
        }
        // allow the user to report a message, only one report per message
        binding.report.isEnabled = true
        // display the message to be reported
        binding.reportedMessage.text = message
        // put the categories for report in the spinner
        val items: List<String> = listOf("Dangerous or Illegal", "Discriminatory", "Misinformation", "Disrespectful", "Other")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, items)
        binding.reason.adapter = adapter
        // set report button listener
        binding.report.setOnClickListener {
            // get report key
            val newEntry = db.child("Reports").push()
            val key = newEntry.key
            // get current time
            val time = SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").format(Calendar.getInstance().time)
            // add report to database
            db.child("Reports").push()
                .setValue(Report(messageID, message, binding.additionalInfo.editText?.text.toString(), reportedUID, reportingUID, time, false))
                .addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            // if report successfully submitted, disable report button
                            binding.report.isEnabled = false
                            // notify user report was submitted
                            Toast.makeText(this, "Report submitted", Toast.LENGTH_SHORT).show()
                            // update messages and contacts to reflect changes
                            db.child("Messages").child(messageID).child("isReported").setValue(true)
                            db.child("Users").child(reportingUID).child("Contacts").child(reportedUID).child("isReported").setValue(true)

                        } else {
                            // notify user that the report failed to be submitted
                            Toast.makeText(this, "Report failed", Toast.LENGTH_SHORT).show()
                        }
                    })


        }

    }
}