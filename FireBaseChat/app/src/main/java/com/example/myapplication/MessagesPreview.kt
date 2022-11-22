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
    var list = ArrayList<MessagePreview>()
    lateinit var binding: MessagesPreviewBinding
    lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
    lateinit var adapter: RecyclerViewAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MessagesPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        var uID = user?.uid
        adapter = RecyclerViewAdapter2(this, list)
        val llm: LinearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerview2.layoutManager = llm
        binding.recyclerview2.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        receiveMessages()
    }



    fun receiveMessages(){
        Log.i("debug",auth.currentUser?.uid!!)
        db.child("Users").child(auth.currentUser?.uid!!).child("Contacts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                list.clear()
                for (snap in snapshot.children){

                    val msg = snap.getValue(MessagePreview::class.java)

                    if (msg != null) {
                        list.add(msg)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })
    }

}

