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
    // Figure out a way to get target ID

    var target = ""


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
        val extras = intent.extras
        if (extras != null) {
            target = extras.getString("target")!!
            db.child("Users").child(target).get().addOnCompleteListener(){
                Log.i("test",it.result.child("Username").value.toString())
                binding.target.text = it.result.child("Username").value.toString()


            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }

        var user = auth.currentUser
        var uID = user?.uid
        var targetUID = target

        binding.fabSend.setOnClickListener(View.OnClickListener {
            //val time = SimpleDateFormat("dd/MM/yyyy h:mm:ss z").format(Calendar.getInstance().time)

            val sdf = SimpleDateFormat("dd/MM/yyyy h:mm:ss z")
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val time = sdf.format(Date())

            if (filterMessage(binding.message.editText?.text.toString())){
                Toast.makeText(this, "Your message contains a word that violates our terms of service", Toast.LENGTH_SHORT).show()
            } else {
                var newEntry = db.child("Messages").push().key
                db.child("Messages").child(newEntry!!)
                    .setValue(Message(uID!!, targetUID, binding.message.editText?.text.toString(), time, false, newEntry!!))
                    .addOnCompleteListener(
                        OnCompleteListener {
                            var length = binding.message.editText?.text.toString().length
                            if(length > 50){
                                length = 50
                            }
                            db.child("Users").child(uID).child("Contacts").child(targetUID)
                                .setValue(MessagePreview(binding.target.text.toString(), targetUID, binding.message.editText?.text.toString().substring(0,length), newEntry, time, false, true))
                            db.child("Users").child(uID).get().addOnCompleteListener(){
                                db.child("Users").child(targetUID).child("Contacts").child(uID)
                                    .setValue(MessagePreview(it.result.child("Username").value.toString(), uID, binding.message.editText?.text.toString().substring(0,length), newEntry, time, false, false))
                                binding.message.editText?.setText("")

                            }.addOnFailureListener{
                                Log.e("firebase", "Error getting data", it)
                            }

                            })

            }

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

                            if((!msg.getIsReported() || msg.getIsReported() && msg.getUserID() == auth.uid) && ((msg.getTargetUID() == target && msg.getUserID() == auth.uid) || (msg.getTargetUID() == auth.uid && msg.getUserID() == target))){
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
            val child: TextView = par.getChildAt(0) as TextView
            val intent = Intent(this, ReportMessage::class.java)
            db.child("Messages").child(child.text.toString()).get().addOnCompleteListener(){
                intent.putExtra("message",it.result.child("msg").value.toString())
                intent.putExtra("messageID",it.result.child("messageID").value.toString())
                intent.putExtra("reportedUID",it.result.child("userID").value.toString())
                intent.putExtra("reportingUID",it.result.child("targetUID").value.toString())
                Log.i("testGet",it.result.child("userID").value.toString())
                Log.i("testGet",it.result.child("targetUID").value.toString())
                startActivity(intent)
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }

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
