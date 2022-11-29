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

        binding = ActivityLoginRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().reference


        val login = binding.login
        login.setOnClickListener(View.OnClickListener {
            val emailText: String = binding.email.editText?.text.toString()
            val passwordText: String = binding.password.editText?.text.toString()
            mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(
                OnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this, Homescreen::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                    }
                })
        })

        val register = binding.register
        register.setOnClickListener(View.OnClickListener {
            binding.emailText.visibility = View.VISIBLE
            binding.passwordText.visibility = View.VISIBLE
            binding.next.visibility = View.VISIBLE
            binding.login.visibility = View.GONE
            binding.register.visibility = View.GONE
            binding.filler.visibility = View.GONE
            binding.forgotPassword.visibility = View.GONE
        })

        val next = binding.next
        next.setOnClickListener(View.OnClickListener {
            val emailText: String = binding.email.editText?.text.toString()
            val passwordText: String = binding.password.editText?.text.toString()
            mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(
                OnCompleteListener { it1 ->
                    if (it1.isSuccessful) {
                        binding.emailText.visibility = View.GONE
                        binding.passwordText.visibility = View.GONE
                        binding.next.visibility = View.GONE
                        binding.email.visibility = View.GONE
                        binding.password.visibility = View.GONE
                        binding.next.visibility = View.GONE
                        binding.submit.visibility = View.VISIBLE
                        binding.usernameText.visibility = View.VISIBLE
                        binding.enterUsername.visibility = View.VISIBLE
                        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                        binding.enterUsername.visibility = View.VISIBLE
                        binding.login.visibility = View.GONE
                        mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(
                            OnCompleteListener {
                                if(it.isSuccessful){

                                } else {
                                    Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()

                                }
                            })

                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                })

        })


        val submit = binding.submit
        submit.setOnClickListener(View.OnClickListener {
            if(binding.enterUsername.editText?.text.toString() == ""){
                Toast.makeText(this, "Enter a valid username", Toast.LENGTH_SHORT).show()
            } else if(filterMessage(binding.enterUsername.editText?.text.toString())){
                Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show()
            } else {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(binding.enterUsername.editText?.text.toString())
                    .build()
                mAuth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener(OnCompleteListener {
                    db.child("Users").child(mAuth.currentUser!!.uid).child("Username")
                        .setValue(binding.enterUsername.editText?.text.toString())
                        .addOnCompleteListener(
                            OnCompleteListener {
                                startActivity(Intent(this, Homescreen::class.java))
                                finish()})
                })

            }

        })

        val forgot = binding.forgotPassword
        forgot.setOnClickListener(View.OnClickListener {
            binding.password.visibility = View.GONE
            binding.emailText.visibility = View.VISIBLE
            binding.login.visibility = View.GONE
            binding.register.visibility = View.GONE
            binding.forgotPassword.visibility = View.GONE
            binding.forgotPasswordSubmit.visibility = View.VISIBLE
            binding.revert.visibility = View.VISIBLE
        })

        val revert = binding.revert
        revert.setOnClickListener( View.OnClickListener {
            binding.password.visibility = View.VISIBLE
            binding.emailText.visibility = View.GONE
            binding.login.visibility = View.VISIBLE
            binding.register.visibility = View.VISIBLE
            binding.forgotPassword.visibility = View.VISIBLE
            binding.forgotPasswordSubmit.visibility = View.GONE
            binding.revert.visibility = View.GONE
        })
        val forgotSub = binding.forgotPasswordSubmit
        forgotSub.setOnClickListener(View.OnClickListener {
            mAuth.sendPasswordResetEmail(binding.email.editText?.text.toString()).addOnCompleteListener(
                OnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                        binding.password.visibility = View.VISIBLE
                        binding.emailText.visibility = View.GONE
                        binding.login.visibility = View.VISIBLE
                        binding.register.visibility = View.VISIBLE
                        binding.forgotPassword.visibility = View.VISIBLE
                        binding.forgotPasswordSubmit.visibility = View.GONE
                        binding.revert.visibility = View.GONE
                    } else {
                        Toast.makeText(this, "No email found", Toast.LENGTH_SHORT).show()
                    }
                })
        })
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser != null){
           startActivity(Intent(this, Homescreen::class.java))
           finish()
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