package com.example.myapplication

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


// Contains the initial home screen fragment based in the
// home activity. Includes 2 card views that that use
// recycler views in order to get and view recent messages
// and unanswered surveys

class HomeFragment : Fragment() {
    var list = ArrayList<Contacts>()
    var survList = ArrayList<String>()
    private lateinit var mAuth: FirebaseAuth
    private var unread = 0
    lateinit var adapter: RecyclerViewHome
    lateinit var survAdapter: RecyclerViewSurveys
    private lateinit var binding: FragmentHomeBinding
    private lateinit var survListener: ValueEventListener
    private lateinit var listener: ValueEventListener



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // initialise other necessary variables
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        adapter = context?.let { RecyclerViewHome(it, list) }!!
        survAdapter = context?.let { RecyclerViewSurveys(it,survList) }!!
        val llm: LinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val sllm: LinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recyclerviewhome.layoutManager = llm
        binding.recyclerviewhome.adapter = adapter

        binding.recyclerviewsurvey.layoutManager = sllm
        binding.recyclerviewsurvey.adapter = survAdapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // register listener for message preview
        receiveMessages()
        // register listener for available surveys
        getSurveys()
    }

    private fun receiveMessages() {
        // listener for users contacts
        listener = db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var x = 0
                list.clear()
                // if messages exist
                if(snapshot.childrenCount > 0) {
                    // remove filler string and show previews
                    binding.nomessages.visibility = View.GONE
                    binding.messageprev.visibility = View.VISIBLE
                    for (snap in snapshot.children) {
                        // get most recent 3 if more than 3 exist
                        if (snapshot.childrenCount >= 3 && x in (snapshot.childrenCount-3)..snapshot.childrenCount) {
                            // get contact
                            val msg = snap.getValue(Contacts::class.java)
                            // check is message exists
                            if (msg != null) {

                                if(!msg.getIsBlocked()){
                                    list.add(msg)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                            // if there are less than 3 recents
                        } else if (snapshot.childrenCount in 1..2) {
                            val msg = snap.getValue(Contacts::class.java)
                            if (msg != null) {

                                if (!msg.getIsRead()) {
                                    unread += 1
                                }
                                if(!msg.getIsBlocked()){
                                    list.add(msg)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                        x++
                    }
                // no messages available
                } else {
                    //
                    binding.messageprev.visibility = View.GONE
                    binding.nomessages.visibility = View.VISIBLE
                }
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }

        })

    }

    private fun getSurveys() {
        // listener for answered surveys
        survListener = db.child("Views").child(mAuth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // if not all surveys are answered
                if (snapshot.childrenCount != 5L) {
                    // remove filler text
                    binding.surveyprev.visibility = View.VISIBLE
                    binding.nosurveys.visibility = View.GONE
                    survList.clear()
                    var i = 1
                    for (snap in snapshot.children) {
                        // if child has answers
                        if (snap.child(i.toString()) == null) {
                            survList.add(i.toString())
                            survAdapter.notifyDataSetChanged()
                        }
                        i++
                    }
                } else {
                    // show filler text if no surveys displayed
                    binding.surveyprev.visibility = View.GONE
                    binding.nosurveys.visibility = View.VISIBLE
                    // close listener
                    db.child("Views").child(mAuth.currentUser!!.uid).removeEventListener(survListener)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })
    }
    
    override fun onPause() {
        super.onPause()
        // close listeners
        db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").removeEventListener(listener)
        db.child("Views").child(mAuth.currentUser!!.uid).removeEventListener(survListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // close listeners
        db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").removeEventListener(listener)
        db.child("Views").child(mAuth.currentUser!!.uid).removeEventListener(survListener)
    }

}