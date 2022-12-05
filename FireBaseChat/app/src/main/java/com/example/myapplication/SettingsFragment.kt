package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SettingsFragment : Fragment() {
    // declare variables
    var list = ArrayList<Contacts>()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mAuth: FirebaseAuth
    lateinit var adapter: RecyclerViewBlocklist
    lateinit var listener: ValueEventListener

    // function to recieve changes to current user's contacts
    fun receiveContacts(){
        // register listener
        listener = db.child("Users").child(mAuth.currentUser?.uid!!).child("Contacts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                // when data is changed
                // remove currently displayed contacts
                list.clear()
                for (snap in snapshot.children){
                    // for each contact, save the current contact as instance of Contacts class
                    val contact = snap.getValue(Contacts::class.java)
                    // if contact is valid
                    if (contact != null) {
                        // display contact
                        list.add(contact)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize variables
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        // intialize recylcer view
        adapter = context?.let { RecyclerViewBlocklist(it, list) }!!
        val llm: LinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.blocklistRecycler.layoutManager = llm
        binding.blocklistRecycler.adapter = adapter

        // get current username and display it then call recieveContacts
        db.child("Users").child(mAuth.currentUser!!.uid).get().addOnCompleteListener(){
            binding.helloUser.text = getString(R.string.hello_settings)+" "+it.result.child("Username").value.toString()+"!"

        }
        // add listener to viewBlocklist button
        binding.viewBlocklist.setOnClickListener(View.OnClickListener {
            // hide all UI
            hideAllUI()
            // display relevant UI including recycler view

            binding.blocklistRecycler.visibility = View.VISIBLE
            binding.returnBlock.visibility = View.VISIBLE
            binding.layout.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        })
        // add listener to changeEmail button
        binding.changeEmail.setOnClickListener(View.OnClickListener {
            // hide all UI
            hideAllUI()
            // display relevant UI including new email text field
            binding.email.visibility = View.VISIBLE
            binding.password.visibility = View.VISIBLE
            binding.newEmail.visibility = View.VISIBLE
            binding.enterNewEmail.visibility = View.VISIBLE
            binding.confirmEmail.visibility = View.VISIBLE
            binding.submitEmailChange.visibility = View.VISIBLE
            binding.returnHome.visibility = View.VISIBLE
            binding.blankButtons.visibility = View.VISIBLE
            binding.signIn.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.blankSignin.visibility = View.VISIBLE
        })
        // add listener to changePassword button
        binding.changePassword.setOnClickListener(View.OnClickListener {
            // hide all UI
            hideAllUI()
            // display relevant UI including new password text field
            binding.email.visibility = View.VISIBLE
            binding.password.visibility = View.VISIBLE
            binding.newPassword.visibility = View.VISIBLE
            binding.enterNewPassword.visibility = View.VISIBLE
            binding.confirmPassword.visibility = View.VISIBLE
            binding.submitPasswordChange.visibility = View.VISIBLE
            binding.returnHome.visibility = View.VISIBLE
            binding.blankButtons.visibility = View.VISIBLE
            binding.signIn.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.blankSignin.visibility = View.VISIBLE
        })
        // add listener to changeUser button
        binding.changeUser.setOnClickListener(View.OnClickListener {
            // hide all UI
            hideAllUI()
            // display relevant UI including new username text field
            binding.email.visibility = View.VISIBLE
            binding.password.visibility = View.VISIBLE
            binding.newUser.visibility = View.VISIBLE
            binding.enterNewUser.visibility = View.VISIBLE
            binding.confirmUser.visibility = View.VISIBLE
            binding.submitUserChange.visibility = View.VISIBLE
            binding.returnHome.visibility = View.VISIBLE
            binding.blankButtons.visibility = View.VISIBLE
            binding.signIn.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.blankSignin.visibility = View.VISIBLE
        })
        // add listener to submitEmailChange button
        binding.submitEmailChange.setOnClickListener(View.OnClickListener {
            // sign in the user before making important account changes
            mAuth.signInWithEmailAndPassword(binding.email.editText!!.text.toString(),binding.password.editText!!.text.toString()).addOnCompleteListener(){ task ->
                    if (task.isSuccessful) {
                        // Sign in success now update email
                        if(binding.newEmail.editText!!.text.toString() == binding.confirmEmail.editText!!.text.toString()){
                            // if emails match each other
                            mAuth.currentUser!!.updateEmail(binding.newEmail.editText!!.text.toString())
                                .addOnCompleteListener{ task ->
                                    if (task.isSuccessful) {
                                        // email update completed
                                        Toast.makeText(context,getString(R.string.success),Toast.LENGTH_SHORT).show()
                                        binding.email.editText?.setText("")
                                        binding.password.editText?.setText("")
                                        binding.newEmail.editText?.setText("")
                                    }else{
                                        // email update failed
                                        Toast.makeText(context,getString(R.string.failed),Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            // emails don't match
                            Toast.makeText(context, getString(R.string.emails_dont_match),Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        // sign in failed
                        Toast.makeText(context,getString(R.string.incorrect_login),Toast.LENGTH_SHORT).show()
                    }
                }
        })
        // add listener to submitPasswordChange button
        binding.submitPasswordChange.setOnClickListener(View.OnClickListener {
            // sign in the user before making important account changes
            mAuth.signInWithEmailAndPassword(binding.email.editText!!.text.toString(),binding.password.editText!!.text.toString()).addOnCompleteListener(){ task ->
                if (task.isSuccessful) {
                    // Sign in success now update password
                    if(binding.newPassword.editText!!.text.toString() == binding.confirmPassword.editText!!.text.toString()){
                        // if passwords match each other
                        mAuth.currentUser!!.updatePassword(binding.newPassword.editText!!.text.toString())
                            .addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    // password update completed
                                    Toast.makeText(context,getString(R.string.success),Toast.LENGTH_SHORT).show()
                                    binding.email.editText?.setText("")
                                    binding.password.editText?.setText("")
                                    binding.newPassword.editText?.setText("")
                                }else{
                                    // password update failed
                                    Toast.makeText(context,getString(R.string.failed),Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // passwords don't match
                        Toast.makeText(context,getString(R.string.passwords_dont_match),Toast.LENGTH_SHORT).show()
                    }

                } else {
                    // sign in failed
                    Toast.makeText(context,getString(R.string.incorrect_login),Toast.LENGTH_SHORT).show()
                }
            }
        })
        // add listener to submitUserChange button
        binding.submitUserChange.setOnClickListener(View.OnClickListener {
            // sign in the user before making important account changes
            mAuth.signInWithEmailAndPassword(binding.email.editText!!.text.toString(),binding.password.editText!!.text.toString()).addOnCompleteListener(){ task ->
                if (task.isSuccessful) {
                    // sign in success now update username
                    // check username format
                    if(binding.newUser.editText!!.text.toString() == ""){
                        // if empty notify user to enter a different username
                        Toast.makeText(context, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show()
                    } else if(binding.newUser.editText!!.text.toString().length > 12){
                        // if longer than 12 characters notify user to enter a different username
                        Toast.makeText(context, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show()
                    } else if(filterMessage(binding.newUser.editText!!.text.toString())){
                        // if it contains a banned word notify user to enter a different username
                        Toast.makeText(context, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show()
                    } else {
                        if(binding.newUser.editText!!.text.toString() == binding.confirmUser.editText!!.text.toString()){
                            // if usernames match
                            // update username in database
                            db.child("Users").child(mAuth.currentUser!!.uid).child("Username")
                                .setValue(binding.newUser.editText?.text.toString())
                                .addOnCompleteListener(
                                    OnCompleteListener {
                                        // on completion update ui to display new username and notify user of success
                                        binding.helloUser.text = getString(R.string.hello_settings)+" "+binding.newUser.editText!!.text.toString()+"!"
                                        Toast.makeText(context, getString(R.string.success), Toast.LENGTH_SHORT).show()
                                        binding.email.editText?.setText("")
                                        binding.password.editText?.setText("")
                                        binding.newUser.editText?.setText("")
                                    })
                        } else {
                            // usernames do not match
                            Toast.makeText(context,getString(R.string.usernames_dont_match),Toast.LENGTH_SHORT).show()
                        }


                    }
                } else {
                    // sign in failed
                    Toast.makeText(context,getString(R.string.incorrect_login),Toast.LENGTH_SHORT).show()
                }
            }
        })
        // add listener to return to home of settings fragment
        binding.returnHome.setOnClickListener(View.OnClickListener {
            resetUI()
        })
        // add listener to return to home of settings fragment from view blocklist
        binding.returnBlock.setOnClickListener(View.OnClickListener {
            resetUI()
            binding.layout.gravity = Gravity.CENTER
        })
        // add listener to signout button
        binding.signOut.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
            startActivity(Intent(context, LoginRegistration::class.java))
        })
            return binding.root
        }
    override fun onResume() {
        super.onResume()
        receiveContacts()
    }

        // function to hide and remove text input from all views in layout
        fun hideAllUI(){
            binding.blankButtons.visibility = View.GONE
            binding.blocklistRecycler.visibility = View.GONE
            binding.email.editText?.setText("")
            binding.password.editText?.setText("")
            binding.newEmail.editText?.setText("")
            binding.newUser.editText?.setText("")
            binding.newPassword.editText?.setText("")
            binding.confirmEmail.editText?.setText("")
            binding.confirmPassword.editText?.setText("")
            binding.confirmUser.editText?.setText("")
            binding.email.visibility = View.GONE
            binding.password.visibility = View.GONE
            binding.newEmail.visibility = View.GONE
            binding.newPassword.visibility = View.GONE
            binding.confirmEmail.visibility = View.GONE
            binding.confirmPassword.visibility = View.GONE
            binding.enterNewEmail.visibility = View.GONE
            binding.submitEmailChange.visibility = View.GONE
            binding.enterNewPassword.visibility = View.GONE
            binding.returnHome.visibility = View.GONE
            binding.submitPasswordChange.visibility = View.GONE
            binding.newUser.visibility = View.GONE
            binding.enterNewUser.visibility = View.GONE
            binding.confirmUser.visibility = View.GONE
            binding.submitUserChange.visibility = View.GONE
            binding.changeEmail.visibility = View.GONE
            binding.helloUser.visibility = View.GONE
            binding.blank1.visibility = View.GONE
            binding.blank2.visibility = View.GONE
            binding.blank3.visibility = View.GONE
            binding.blank4.visibility = View.GONE
            binding.blank5.visibility = View.GONE
            binding.changePassword.visibility = View.GONE
            binding.changeUser.visibility = View.GONE
            binding.viewBlocklist.visibility = View.GONE
            binding.buttons.visibility = View.GONE
            binding.signIn.visibility = View.GONE
            binding.blankSignin.visibility = View.GONE
            binding.returnBlock.visibility = View.GONE
            binding.signOut.visibility = View.GONE
        }
        // function to display the starting views for settings screen
        fun resetUI(){
            hideAllUI()
            binding.changeEmail.visibility = View.VISIBLE
            binding.helloUser.visibility = View.VISIBLE
            binding.blank1.visibility = View.VISIBLE
            binding.blank2.visibility = View.VISIBLE
            binding.blank3.visibility = View.VISIBLE
            binding.blank4.visibility = View.VISIBLE
            binding.blank5.visibility = View.VISIBLE
            binding.changePassword.visibility = View.VISIBLE
            binding.changeUser.visibility = View.VISIBLE
            binding.viewBlocklist.visibility = View.VISIBLE
            binding.signOut.visibility = View.VISIBLE

        }
        // function to check usernames for banned words
        fun filterMessage(msg: String): Boolean {
            val bannedWords: Set<String> = listOf<String>( "fuck", "shit", "bitch", "nigga", "prostitute", "jew", "blackie", "bastard").toSet()
            for (wrd in bannedWords){
                if(msg.contains(wrd, ignoreCase = true)){
                    return true
                }
            }
            return false
        }

    override fun onDestroyView() {
        super.onDestroyView()
        // unregister listener when view destroyed
        db.child("Users").child(mAuth.currentUser?.uid!!).child("Contacts").removeEventListener(listener)
    }

    }


