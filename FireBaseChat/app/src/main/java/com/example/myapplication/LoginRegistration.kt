package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginRegistrationBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


private lateinit var mAuth: FirebaseAuth;
private lateinit var binding: ActivityLoginRegistrationBinding
lateinit var db : DatabaseReference

class LoginRegistration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializing important variables
        binding = ActivityLoginRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().reference

        // Set the listener for the login button
        val login = binding.login
        login.setOnClickListener(View.OnClickListener {
            // get the email and password entered
            val emailText: String = binding.email.editText?.text.toString()
            val passwordText: String = binding.password.editText?.text.toString()
            // call firebase sign in method and check for a successful sign in
            mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(
                OnCompleteListener {
                    if(it.isSuccessful){
                        // if successful go to home screen
                        startActivity(Intent(this, Homescreen::class.java))
                        finish()
                    } else {
                        // if failed notify user
                        Toast.makeText(this, getString(R.string.incorrect_login), Toast.LENGTH_SHORT).show()
                    }
                })
        })

        // set listener for register button
        val register = binding.register
        register.setOnClickListener(View.OnClickListener {
            // show all necessary UI components
            binding.emailText.visibility = View.VISIBLE
            binding.passwordText.visibility = View.VISIBLE
            binding.next.visibility = View.VISIBLE
            binding.login.visibility = View.GONE
            binding.register.visibility = View.GONE
            binding.filler.visibility = View.GONE
            binding.forgotPassword.visibility = View.GONE
            binding.revert.visibility = View.VISIBLE
        })

        // set listener for next button which is called when the user enters an email and password
        // for a new account
        val next = binding.next
        next.setOnClickListener(View.OnClickListener {
            // get email and password entered
            val emailText: String = binding.email.editText?.text.toString()
            val passwordText: String = binding.password.editText?.text.toString()
            // call firebase createUser method and listen for success
            mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(
                OnCompleteListener { it1 ->
                    // if successful prompt the user to enter a username and notify them it worked
                    if (it1.isSuccessful) {
                        // hide irrelevant UI
                        binding.emailText.visibility = View.GONE
                        binding.passwordText.visibility = View.GONE
                        binding.next.visibility = View.GONE
                        binding.email.visibility = View.GONE
                        binding.password.visibility = View.GONE
                        binding.next.visibility = View.GONE
                        // notify user of successful registration
                        Toast.makeText(this, getString(R.string.created_success), Toast.LENGTH_SHORT).show()
                        // show username input UI
                        binding.submit.visibility = View.VISIBLE
                        binding.usernameText.visibility = View.VISIBLE
                        binding.enterUsername.visibility = View.VISIBLE
                        binding.enterUsername.visibility = View.VISIBLE
                        binding.login.visibility = View.GONE
                        binding.revert.visibility = View.GONE
                        // sign the user in so we can set username
                        mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(
                            OnCompleteListener {
                                if(it.isSuccessful){

                                } else {
                                    // if sign in failed notify user
                                    Toast.makeText(this, getString(R.string.incorrect_login), Toast.LENGTH_SHORT).show()

                                }
                            })

                    } else {
                        // notify user that registration failed
                        Toast.makeText(this, getString(R.string.registration_failed), Toast.LENGTH_SHORT).show()
                    }
                })

        })

        // set listener for submit username button
        val submit = binding.submit
        submit.setOnClickListener(View.OnClickListener {
            // check username validity
            if(binding.enterUsername.editText?.text.toString() == ""){
                // if empty notify user to enter a different username
                Toast.makeText(this, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show()
            } else if(binding.enterUsername.editText?.text.toString().length > 12){
                // if longer than 12 characters notify user to enter a different username
                Toast.makeText(this, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show()
            } else if(filterMessage(binding.enterUsername.editText?.text.toString())){
                // if it contains a banned word notify user to enter a different username
                Toast.makeText(this, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show()
            } else {
                // if valid, add username to current user to firebase profile
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(binding.enterUsername.editText?.text.toString())
                    .build()
                mAuth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener(OnCompleteListener {
                    // add username to our users database
                    db.child("Users").child(mAuth.currentUser!!.uid).child("Username")
                        .setValue(binding.enterUsername.editText?.text.toString())
                        .addOnCompleteListener(
                            OnCompleteListener {
                                // if this succeeded start home screen activity
                                startActivity(Intent(this, Homescreen::class.java))
                                finish()})
                })

            }

        })

        // set on click listener for forgot password button
        val forgot = binding.forgotPassword
        forgot.setOnClickListener(View.OnClickListener {
            // show UI to prompt user to enter their email
            binding.password.visibility = View.GONE
            binding.emailText.visibility = View.VISIBLE
            binding.login.visibility = View.GONE
            binding.register.visibility = View.GONE
            binding.forgotPassword.visibility = View.GONE
            binding.forgotPasswordSubmit.visibility = View.VISIBLE
            binding.revert.visibility = View.VISIBLE

        })

        // set listener for submitting password reset email
        val forgotSub = binding.forgotPasswordSubmit
        forgotSub.setOnClickListener(View.OnClickListener {
            // call send password reset email and check for success
            mAuth.sendPasswordResetEmail(binding.email.editText?.text.toString()).addOnCompleteListener(
                OnCompleteListener {
                    if (it.isSuccessful){
                        // if it succeeded notify the user and return to login screen
                        Toast.makeText(this, getString(R.string.email_sent), Toast.LENGTH_SHORT).show()
                        binding.password.visibility = View.VISIBLE
                        binding.emailText.visibility = View.GONE
                        binding.login.visibility = View.VISIBLE
                        binding.register.visibility = View.VISIBLE
                        binding.forgotPassword.visibility = View.VISIBLE
                        binding.forgotPasswordSubmit.visibility = View.GONE
                        binding.revert.visibility = View.GONE
                        binding.submit.visibility = View.GONE
                        binding.usernameText.visibility = View.GONE
                        binding.enterUsername.visibility = View.GONE
                        binding.enterUsername.visibility = View.GONE
                        binding.emailText.visibility = View.GONE
                        binding.passwordText.visibility = View.GONE
                        binding.next.visibility = View.GONE
                        binding.filler.visibility = View.VISIBLE
                    } else {
                        // if it failed notify user that email was not found
                        Toast.makeText(this, getString(R.string.email_failed), Toast.LENGTH_SHORT).show()
                    }
                })
        })

        // go back from forgot password or registration by reverting to original UI
        val revert = binding.revert
        revert.setOnClickListener( View.OnClickListener {
            binding.password.visibility = View.VISIBLE
            binding.emailText.visibility = View.GONE
            binding.login.visibility = View.VISIBLE
            binding.register.visibility = View.VISIBLE
            binding.forgotPassword.visibility = View.VISIBLE
            binding.forgotPasswordSubmit.visibility = View.GONE
            binding.revert.visibility = View.GONE
            binding.submit.visibility = View.GONE
            binding.usernameText.visibility = View.GONE
            binding.enterUsername.visibility = View.GONE
            binding.enterUsername.visibility = View.GONE
            binding.emailText.visibility = View.GONE
            binding.passwordText.visibility = View.GONE
            binding.next.visibility = View.GONE
            binding.filler.visibility = View.VISIBLE
        })


    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in and then proceed to home screen if they are
        val currentUser = mAuth.currentUser
        if (currentUser != null){
           startActivity(Intent(this, Homescreen::class.java))
           finish()

        }
    }

    // function to check if a string contains banned words
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