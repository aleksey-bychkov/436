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

        // run 4-nearest neighbors algorithm on view responses to find the best people to talk to
        // we have decided that users are most likely to have a productive conversation with people
        // somewhat similarly minded, which is why we look for people somewhat similar
        var choice1: String? = null
        var choice2: String? = null
        var choice3: String? = null
        var choice4: String? = null
        var agg1 = 10000000F
        var agg2 = 10000000F
        var agg3 = 10000000F
        var agg4 = 10000000F

        // register listener to Views
        val listener = db.child("Views").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                // for each child in views
                for (snap in snapshot.children){
                    // get child as an instance of ViewResponse class
                    val resp = snap.child(topicID).getValue(ViewResponse::class.java)
                    // if the response is valid
                    if (resp != null) {
                        // check if the response comes from the current user
                        if(resp.getUID() != mAuth.currentUser!!.uid){
                            // if the response is closer than current 4th closest response
                            if(Math.abs(resp.getAgg()-aggScore) < agg1){
                                // if the response is closer than current 3rd closest response
                                if(Math.abs(resp.getAgg()-aggScore) < agg2){
                                    // if the response is closer than current 2nd closest response
                                    if(Math.abs(resp.getAgg()-aggScore) < agg3){
                                        // if the response is closer than closest response
                                        if(Math.abs(resp.getAgg()-aggScore) < agg4){
                                            // move all other responses down one tier
                                            agg1 = agg2
                                            choice1 = choice2
                                            agg2 = agg3
                                            choice2 = choice3
                                            agg3 = agg4
                                            choice3 = choice4
                                            // save current response as closest
                                            agg4 = resp.getAgg()
                                            choice4 = resp.getUID()
                                        } else {
                                            // move all other responses down one tier
                                            agg1 = agg2
                                            choice1 = choice2
                                            agg2 = agg3
                                            choice2 = choice3
                                            // set current response as 2nd closest
                                            agg3 = resp.getAgg()
                                            choice3 = resp.getUID()
                                        }
                                    } else {
                                        // move all other responses down one tier
                                        agg1 = agg2
                                        choice1 = choice2
                                        // set current response as 4th closest
                                        agg2 = resp.getAgg()
                                        choice2 = resp.getUID()
                                    }
                                } else {
                                    // set current response as 4th closest
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
        // remove listener
        db.child("Views").removeEventListener(listener)

        // check if someone was found
        if(choice4 != null){
            // get username of closest user and set the text of person1 button to it
            db.child("Users").child(choice4!!).get().addOnCompleteListener(){
                binding.person1.text = it.result.child("Username").value.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        } else {
            // if no closest user hide button
            binding.person1.visibility = View.GONE
        }

        // check if someone was found
        if(choice3 != null){
            // get username of 2nd closest user and set the text of person2 button to it
            db.child("Users").child(choice3!!).get().addOnCompleteListener(){
                binding.person2.text = it.result.child("Username").value.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }  else {
            // if no 2nd closest user hide button
            binding.person2.visibility = View.GONE
        }

        // check if someone was found
        if(choice2 != null){
            // get username of 3rd closest user and set the text of person3 button to it
            db.child("Users").child(choice2!!).get().addOnCompleteListener(){
                binding.person3.text = it.result.child("Username").value.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }  else {
            // if no 3rd closest user hide button
            binding.person3.visibility = View.GONE
        }

        // check if someone was found
        if(choice1 != null){
            // get username of 4th closest user and set the text of person4 button to it
            db.child("Users").child(choice1!!).get().addOnCompleteListener(){
                binding.person4.text = it.result.child("Username").value.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }  else {
            // if no 4th closest user hide button
            binding.person4.visibility = View.GONE
        }

        // set onClick listeners for each person button
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
        // Save the desired person as a one way contact, with an identifier to specify no messages
        // have been sent between these users
        db.child(mAuth.currentUser!!.uid).child("Contacts").child(choice!!).setValue(Contacts(binding.person1.text.toString(), choice, "", "temp", "", false, true, false)).addOnCompleteListener(
            OnCompleteListener {
                // when this is complete launch messaging activity
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("target",choice)
                context?.let { ContextCompat.startActivity(it, intent, null) }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}


