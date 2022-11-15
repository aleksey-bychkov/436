package com.example.myapplication

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginRegistrationBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


private lateinit var mAuth: FirebaseAuth;
private lateinit var binding: ActivityLoginRegistrationBinding

class LoginRegistration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance();


        val login = binding.login
        login.setOnClickListener(View.OnClickListener {
            val emailText: String = binding.email.editText?.text.toString()
            val passwordText: String = binding.password.editText?.text.toString()
            mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(
                OnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                    }
                })
        })

        val register = binding.register
        register.setOnClickListener(View.OnClickListener {
            val emailText: String = binding.email.editText?.text.toString()
            val passwordText: String = binding.password.editText?.text.toString()
            mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(
                OnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            })
        })
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
       if (currentUser != null){
           startActivity(Intent(this, MainActivity::class.java))
           finish()
       }
    }
}