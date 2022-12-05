package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityLoginRegistrationBinding
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.HomescreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    var list = ArrayList<Contacts>()
    private lateinit var mAuth: FirebaseAuth
    private var unread = 0
    lateinit var adapter: RecyclerViewHome
    private lateinit var binding: FragmentHomeBinding
    private lateinit var listener: ValueEventListener



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        adapter = context?.let { RecyclerViewHome(it, list) }!!
        val llm: LinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recyclerviewhome.layoutManager = llm
        binding.recyclerviewhome.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveMessages()

    }

    private fun receiveMessages() {
        listener = db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                unread = 0
                var i = 0
                list.clear()
                if(snapshot.childrenCount > 0) {
                    binding.nomessages.visibility = View.GONE
                    binding.messageprev.visibility = View.VISIBLE
                    for (snap in snapshot.children) {
                        if (snapshot.childrenCount > 3 && i in (snapshot.childrenCount-1)..snapshot.childrenCount) {
                            Log.i("Here", "it wokrs")
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
    
    override fun onPause() {
        super.onPause()
        db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").removeEventListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").removeEventListener(listener)
    }

}