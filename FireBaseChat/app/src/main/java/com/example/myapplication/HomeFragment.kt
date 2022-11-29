package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mAuth: FirebaseAuth
    private var unread = 0
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        db.child("Users").child(mAuth.currentUser!!.uid).child("Contacts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                unread = 0
                for (snap in snapshot.children) {
                    val msg = snap.getValue(Contacts::class.java)

                    if (msg != null) {

                        if (!msg.getIsRead()) {
                            unread += 1
                        }
                    }
                }
                db.child("Users").child(mAuth.currentUser!!.uid).get().addOnCompleteListener(){
                    Log.i("test",it.result.child("Username").value.toString())
                    val name = it.result.child("Username").value.toString()
                    val text: String = getString(R.string.welcome_messages, name, unread)
                    view?.findViewById<TextView>(R.id.messages)?.setText(text)
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }

        })


        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}