package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentSurveyResultsBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SurveyResultsFragment(topicID: String, aggScore: Float) : Fragment() {

    private lateinit var binding: FragmentSurveyResultsBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private var topicID = topicID
    private var aggScore = aggScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentSurveyResultsBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        var choice1: String? = null
        var choice2: String? = null
        var choice3: String? = null
        var choice4: String? = null
        var agg1 = 10000000F
        var agg2 = 10000000F
        var agg3 = 10000000F
        var agg4 = 10000000F

        val listener = db.child("Views").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                for (snap in snapshot.children){
                    val resp = snap.child(topicID).getValue(ViewResponse::class.java)

                    if (resp != null) {
                        if(resp.getUID() != mAuth.currentUser!!.uid){
                            if(Math.abs(resp.getAgg()-aggScore) < agg1){
                                if(Math.abs(resp.getAgg()-aggScore) < agg2){
                                    if(Math.abs(resp.getAgg()-aggScore) < agg3){
                                        if(Math.abs(resp.getAgg()-aggScore) < agg4){
                                            agg1 = agg2
                                            choice1 = choice2
                                            agg2 = agg3
                                            choice2 = choice3
                                            agg3 = agg4
                                            choice3 = choice4
                                            agg4 = resp.getAgg()
                                            choice4 = resp.getUID()
                                        } else {
                                            agg1 = agg2
                                            choice1 = choice2
                                            agg2 = agg3
                                            choice2 = choice3
                                            agg3 = resp.getAgg()
                                            choice3 = resp.getUID()
                                        }
                                    } else {
                                        agg1 = agg2
                                        choice1 = choice2
                                        agg2 = resp.getAgg()
                                        choice2 = resp.getUID()
                                    }
                                } else {
                                    agg1 = resp.getAgg()
                                    choice1 = resp.getUID()
                                }
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })

        db.removeEventListener(listener)

        if(choice4 != null){
            db.child("Users").child(choice4!!).get().addOnCompleteListener(){
                binding.person1.text = it.result.child("Username").value.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }

        if(choice3 != null){
            db.child("Users").child(choice3!!).get().addOnCompleteListener(){
                binding.person2.text = it.result.child("Username").value.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }  else {
            binding.person2.visibility = View.GONE
        }

        if(choice2 != null){
            db.child("Users").child(choice2!!).get().addOnCompleteListener(){
                binding.person3.text = it.result.child("Username").value.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }  else {
            binding.person3.visibility = View.GONE
        }

        if(choice1 != null){
            db.child("Users").child(choice1!!).get().addOnCompleteListener(){
                binding.person4.text = it.result.child("Username").value.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }  else {
            binding.person4.visibility = View.GONE
        }

        binding.person1.setOnClickListener{
            startConversation(choice4)
        }
        binding.person2.setOnClickListener{
            startConversation(choice3)
        }
        binding.person3.setOnClickListener{
            startConversation(choice2)
        }
        binding.person4.setOnClickListener{
            startConversation(choice1)
        }

        return binding.root
    }

    private fun startConversation( choice: String?)
    {
        // figure this out
        db.child(mAuth.currentUser!!.uid).child("Contacts").child(choice!!).setValue(Contacts(binding.person1.text.toString(), choice, "", "temp", "", false, true, false)).addOnCompleteListener(
            OnCompleteListener {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("target",choice)
                context?.let { ContextCompat.startActivity(it, intent, null) }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}


