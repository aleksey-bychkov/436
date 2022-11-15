package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    // Figure out a way to get target ID
    var target = "targetUID"


    var list = ArrayList<Message>()
    lateinit var binding: ActivityMainBinding
    lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
    lateinit var adapter: RecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        var user = auth.currentUser
        var uID = user?.uid
        var targetUID = "targetUID"
        val time = SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").format(Calendar.getInstance().time)
        binding.fabSend.setOnClickListener(View.OnClickListener {
            db.child("Messages").push()
                .setValue(Message(uID!!, targetUID, binding.message.editText?.text.toString(), time, false))
                .addOnCompleteListener(
                    OnCompleteListener { binding.message.editText?.setText("") })
        })

        adapter = RecyclerViewAdapter(this, list)
        val llm: LinearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerview.layoutManager = llm
        binding.recyclerview.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        receiveMessages()
    }

    fun receiveMessages(){
            db.child("Messages").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                    list.clear()
                    for (snap in snapshot.children){

                        val msg = snap.getValue(Message::class.java)

                        if (msg != null) {

                            if(!msg.getIsReported() && ((msg.getTargetUID() == target && msg.getUserID() == auth.uid) || (msg.getTargetUID() == auth.uid && msg.getUserID() == target))){
                                list.add(msg)
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

        fun reportMessage(v: View): Unit {
            val par: ViewGroup = v.parent as ViewGroup
            val child: TextView = par.getChildAt(1) as TextView
            Log.i("reportButton", child.text.toString())
        }
    }
