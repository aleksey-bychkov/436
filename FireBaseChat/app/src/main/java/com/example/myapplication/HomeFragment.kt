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

    //override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //    super.onViewCreated(view, savedInstanceState)
    //    receiveMessages()

    //    }

    override fun onResume() {
        super.onResume()
        receiveMessages()
        getSurveys()
    }

    private fun receiveMessages() {
        listener = db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                unread = 0
                var x = 0
                list.clear()
                if(snapshot.childrenCount > 0) {
                    binding.nomessages.visibility = View.GONE
                    binding.messageprev.visibility = View.VISIBLE
                    for (snap in snapshot.children) {
                        if (snapshot.childrenCount >= 3 && x in (snapshot.childrenCount-3)..snapshot.childrenCount) {
                            Log.i("Here", "loop")
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

                    }
                } else {
                    binding.messageprev.visibility = View.GONE
                    binding.nomessages.visibility = View.VISIBLE
                }
                //db.child("Users").child(mAuth.currentUser!!.uid).get().addOnCompleteListener(){
                //    Log.i("test",it.result.child("Username").value.toString())
                //    val name = it.result.child("Username").value.toString()
                //    val text: String = getString(R.string.welcome_messages, name, unread)
                //    view?.findViewById<TextView>(R.id.welcome)?.setText(text)
                //}.addOnFailureListener{
                //    Log.e("firebase", "Error getting data", it)
                //}
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }

        })

    }

    private fun getSurveys() {
        survListener = db.child("Views").child(mAuth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount != 5L) {
                    binding.surveyprev.visibility = View.VISIBLE
                    binding.nosurveys.visibility = View.GONE
                    survList.clear()
                    var i = 1
                    for (snap in snapshot.children) {
                        if (snap.child(i.toString()) == null) {
                            survList.add(i.toString())
                            survAdapter.notifyDataSetChanged()
                        }
                        i++
                    }
                } else {
                    binding.surveyprev.visibility = View.GONE
                    binding.nosurveys.visibility = View.VISIBLE
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
        db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").removeEventListener(listener)
        db.child("Views").child(mAuth.currentUser!!.uid).removeEventListener(survListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").removeEventListener(listener)
        db.child("Views").child(mAuth.currentUser!!.uid).removeEventListener(survListener)
    }

}