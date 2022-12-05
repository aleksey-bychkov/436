package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.MessagesPreviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class MessagesPreview: AppCompatActivity() {
    // declaring important variables
    var list = ArrayList<Contacts>()
    lateinit var binding: MessagesPreviewBinding
    lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
    lateinit var adapter: RecyclerViewAdapter2
    lateinit var listener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // intializing variables
        binding = MessagesPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        var uID = user?.uid
        // initializing RecyclerView for message previews
        adapter = RecyclerViewAdapter2(this, list)
        val llm: LinearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerview2.layoutManager = llm
        binding.recyclerview2.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        // register listener for new message previews
        receiveMessages()
    }

    override fun onStop() {
        super.onStop()
        // unregister listener
        db.child("Users").child(auth.currentUser?.uid!!).child("Contacts").removeEventListener(listener)
    }



    fun receiveMessages(){
        // add a listener to the current user's contacts
        listener = db.child("Users").child(auth.currentUser?.uid!!).child("Contacts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                // when the current user's contacts is changed
                list.clear()
                for (snap in snapshot.children){
                    // get the contact as an instance of Contacts class
                    val con = snap.getValue(Contacts::class.java)
                    // if contacts is valid
                    if (con != null) {
                        // if the user has not blocked the contact
                        if(!con.getIsBlocked()){
                            // add contact to adapter
                            list.add(con)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })
    }

}

