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

    // Figure out a way to get target ID
    var target = "targetUID"


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
        binding = ReportMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val extras = intent.extras
        if (extras != null) {
            message = extras.getString("message")!!
            messageID = extras.getString("messageID")!!
            reportedUID = extras.getString("reportedUID")!!
            reportingUID = extras.getString("reportingUID")!!
            //The key argument here must match that used in the other activity
        }
        binding.report.isEnabled = true
        binding.reportedMessage.text = message
        val items: List<String> = listOf("Dangerous or Illegal", "Discriminatory", "Misinformation", "Disrespectful", "Other")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, items)
        binding.reason.adapter = adapter
        binding.report.setOnClickListener {
            val newEntry = db.child("Reports").push()
            val key = newEntry.key
            val time = SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").format(Calendar.getInstance().time)
            db.child("Reports").push()
                .setValue(Report(messageID, message, binding.additionalInfo.editText?.text.toString(), reportedUID, reportingUID, time, false))
                .addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            binding.report.isEnabled = false
                            Toast.makeText(this, "Report submitted", Toast.LENGTH_SHORT).show()
                            db.child("Messages").child(messageID).child("isReported").setValue(true)
                            db.child("Users").child(reportingUID).child("Contacts").child(reportedUID).get().addOnCompleteListener(){
                                if(it.result.child("msgID").value.toString() == messageID){
                                    db.child("Users").child(reportingUID).child("Contacts").child(reportedUID).child("isReported").setValue(true)
                                }



                            }.addOnFailureListener{
                                Log.e("firebase", "Error getting data", it)
                            }

                        } else {
                            Toast.makeText(this, "Report failed", Toast.LENGTH_SHORT).show()
                        }
                    })


        }
        //binding.back.setOnClickListener {
        //    startActivity(Intent(this, MainActivity::class.java))
        //}
    }
}