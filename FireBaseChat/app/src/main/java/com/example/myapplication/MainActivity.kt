package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.System.DATE_FORMAT
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    // declaring important variables
    var target = ""
    var list = ArrayList<Message>()
    lateinit var binding: ActivityMainBinding
    lateinit var db : DatabaseReference
    private lateinit var auth : FirebaseAuth
    lateinit var adapter: RecyclerViewAdapter
    lateinit var listener: ValueEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initializing variables
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        // get the targetID passed in as an extra
        val extras = intent.extras
        if (extras != null) {
            target = extras.getString("target")!!
            // use the targetID to get the target's username from database
            db.child("Users").child(target).get().addOnCompleteListener(){
                // set the header of email to be target's username
                binding.target.text = it.result.child("Username").value.toString()


            }.addOnFailureListener{
                // if this failed output error in log
                Log.e("firebase", "Error getting data", it)
            }

        }


        var user = auth.currentUser
        var uID = user?.uid
        var targetUID = target

        // setting on click listener to check for message being sent
        binding.fabSend.setOnClickListener(View.OnClickListener {
            // get current time in UTC, since this is standard
            val sdf = SimpleDateFormat("dd/MM/yyyy h:mm:ss z")
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val time = sdf.format(Date())
            // check if message is valid
            if (filterMessage(binding.message.editText?.text.toString())){
                // if message contains a banned word notify user and do not send
                Toast.makeText(this, getString(R.string.banned_word), Toast.LENGTH_SHORT).show()
            } else if(binding.message.editText?.text.toString() != ""){
                // if message is not empty, send message and update contacts information for sending and target user
                db.child("Users").child(target).child("Contacts").child(uID!!).get().addOnCompleteListener(){
                    // check if target user has current user blocked
                    if(it.result.child("isBlocked").value == false){
                        // if not blocked add message to database
                        var newEntry = db.child("Messages").push().key
                        db.child("Messages").child(newEntry!!)
                            .setValue(Message(uID!!, targetUID, binding.message.editText?.text.toString(), time, false, newEntry!!))
                            .addOnCompleteListener(
                                OnCompleteListener {
                                    // if message successfully added, update contacts of both users to reflect newest message
                                    // get message preview
                                    var length = binding.message.editText?.text.toString().length
                                    if(length > 50){
                                        length = 50
                                    }
                                    // update current user contacts
                                    db.child("Users").child(uID).child("Contacts").child(targetUID)
                                        .setValue(Contacts(binding.target.text.toString(), targetUID, binding.message.editText?.text.toString().substring(0,length), newEntry, time, false, true, false))
                                    db.child("Users").child(target).child("Contacts").child(uID).get().addOnCompleteListener(){ it2 ->
                                        // get current user's username and update contacts of target user
                                        db.child("Users").child(uID).get().addOnCompleteListener { it3 ->
                                            db.child("Users").child(targetUID).child("Contacts")
                                                .child(uID)
                                                .setValue(
                                                    Contacts(
                                                        it3.result.child("Username").value.toString(),
                                                        uID,
                                                        binding.message.editText?.text.toString()
                                                            .substring(0, length),
                                                        newEntry,
                                                        time,
                                                        false,
                                                        false,
                                                        it2.result.child("isBlocked").value as Boolean
                                                    )
                                                )
                                            // clear text from send message text input
                                            binding.message.editText?.setText("")
                                        }


                                    }.addOnFailureListener{
                                        // if this failed specify why in the error log
                                        Log.e("firebase", "Error getting data", it)
                                    }

                                })
                    } else {
                        // if user is blocked, do not send the message just clear the text box
                        binding.message.editText?.setText("")
                    }


                }.addOnFailureListener{
                    // if this failed specify why in the error log
                    Log.e("firebase", "Error getting data", it)

                }

            }

        })

        // initialize recycler view pieces for message list
        adapter = RecyclerViewAdapter(this, list)
        val llm: LinearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerview.layoutManager = llm
        binding.recyclerview.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        // add event listener to database on start
        receiveMessages()
    }

    override fun onStop() {
        super.onStop()
        // remove event listener on activity stop
        db.child("Messages").removeEventListener(listener)
    }

    fun receiveMessages(){
        // register ValueEventListener under messages to listen to all incoming messages
            listener = db.child("Messages").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                    // clear list of currently displayed messages
                    list.clear()
                    for (snap in snapshot.children){
                        // for each message in database, convert to instance of Message class
                        val msg = snap.getValue(Message::class.java)

                        // if msg is valid
                        if (msg != null) {
                            // check if the msg is intended for the user or sent by the user and if the message was not reported by the user
                            if((!msg.getIsReported() || msg.getIsReported() && msg.getUserID() == auth.uid) && ((msg.getTargetUID() == target && msg.getUserID() == auth.uid) || (msg.getTargetUID() == auth.uid && msg.getUserID() == target))){
                                // add the message to the adapter list and display
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

        // function used for reporting a message
        // only the receiver of a message can report it
        fun reportMessage(v: View): Unit {
            // get the messageID of message being reported
            val par: ViewGroup = v.parent as ViewGroup
            val child: TextView = par.getChildAt(0) as TextView
            val intent = Intent(this, ReportMessage::class.java)
            // add relevant information about the message to the Intent to start the reporting process
            db.child("Messages").child(child.text.toString()).get().addOnCompleteListener(){
                intent.putExtra("message",it.result.child("msg").value.toString())
                intent.putExtra("messageID",it.result.child("messageID").value.toString())
                intent.putExtra("reportedUID",it.result.child("userID").value.toString())
                intent.putExtra("reportingUID",it.result.child("targetUID").value.toString())
                // launch report activity
                startActivity(intent)
            }.addOnFailureListener{
                // failed to get relevant message information
                Log.e("firebase", "Error getting data", it)

            }

        }

        // function to check message for banned words
        fun filterMessage(msg: String): Boolean {
            val bannedWords: Set<String> = listOf<String>( "fuck", "shit", "bitch", "nigga", "prostitute", "jew", "blackie", "bastard").toSet()
            for (wrd in bannedWords){
                if(msg.contains(wrd, ignoreCase = true)){
                    return true
                }
            }
            return false
        }
    }
