package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()

        db.child("Users").child(mAuth.currentUser!!.uid).get().addOnCompleteListener(){
            Log.i("test",it.result.child("Username").value.toString())
            binding.helloUser.text = "Hello, "+it.result.child("Username").value.toString()+"!"

        }

        binding.changeEmail.setOnClickListener(View.OnClickListener {
            hideAllUI()
            binding.email.visibility = View.VISIBLE
            binding.password.visibility = View.VISIBLE
            binding.newEmail.visibility = View.VISIBLE
            binding.enterNewEmail.visibility = View.VISIBLE
            binding.submitEmailChange.visibility = View.VISIBLE
            binding.returnFromEmail.visibility = View.VISIBLE
        })

        binding.changePassword.setOnClickListener(View.OnClickListener {
            hideAllUI()
            binding.email.visibility = View.VISIBLE
            binding.password.visibility = View.VISIBLE
            binding.newPassword.visibility = View.VISIBLE
            binding.enterNewPassword.visibility = View.VISIBLE
            binding.submitPasswordChange.visibility = View.VISIBLE
            binding.returnFromPassword.visibility = View.VISIBLE

        })

        binding.changeUser.setOnClickListener(View.OnClickListener {
            hideAllUI()
            binding.email.visibility = View.VISIBLE
            binding.password.visibility = View.VISIBLE
            binding.newUser.visibility = View.VISIBLE
            binding.enterNewUser.visibility = View.VISIBLE
            binding.submitUserChange.visibility = View.VISIBLE
            binding.returnFromUser.visibility = View.VISIBLE

        })

        binding.submitEmailChange.setOnClickListener(View.OnClickListener {
            mAuth.signInWithEmailAndPassword(binding.email.editText!!.text.toString(),binding.password.editText!!.text.toString()).addOnCompleteListener(){ task ->
                    if (task.isSuccessful) {
                        // Sign in success now update email
                        mAuth.currentUser!!.updateEmail(binding.newEmail.editText!!.text.toString())
                            .addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    // email update completed
                                    Toast.makeText(context,"Success!",Toast.LENGTH_SHORT).show()
                                    binding.email.editText?.setText("")
                                    binding.password.editText?.setText("")
                                    binding.newEmail.editText?.setText("")
                                }else{
                                    // email update failed
                                    Toast.makeText(context,"Failed!",Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // sign in failed
                        Toast.makeText(context,"Invalid login information.",Toast.LENGTH_SHORT).show()
                    }
                }
        })

        binding.submitPasswordChange.setOnClickListener(View.OnClickListener {
            mAuth.signInWithEmailAndPassword(binding.email.editText!!.text.toString(),binding.password.editText!!.text.toString()).addOnCompleteListener(){ task ->
                if (task.isSuccessful) {
                    // Sign in success now update email
                    mAuth.currentUser!!.updatePassword(binding.newPassword.editText!!.text.toString())
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                // email update completed
                                Toast.makeText(context,"Success!",Toast.LENGTH_SHORT).show()
                                binding.email.editText?.setText("")
                                binding.password.editText?.setText("")
                                binding.newPassword.editText?.setText("")
                            }else{
                                // email update failed
                                Toast.makeText(context,"Failed!",Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // sign in failed
                    Toast.makeText(context,"Invalid login information.",Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.submitUserChange.setOnClickListener(View.OnClickListener {
            mAuth.signInWithEmailAndPassword(binding.email.editText!!.text.toString(),binding.password.editText!!.text.toString()).addOnCompleteListener(){ task ->
                if (task.isSuccessful) {
                    // Sign in success now update email
                    if(binding.newUser.editText?.text.toString() == ""){
                        Toast.makeText(context, "Enter a valid username", Toast.LENGTH_SHORT).show()
                    } else if(filterMessage(binding.newUser.editText?.text.toString())){
                        Toast.makeText(context, "Invalid username", Toast.LENGTH_SHORT).show()
                    } else {
                        db.child("Users").child(mAuth.currentUser!!.uid).child("Username")
                            .setValue(binding.newUser.editText?.text.toString())
                            .addOnCompleteListener(
                                OnCompleteListener {
                                   binding.helloUser.text = "Hello, "+binding.newUser.editText?.text.toString()+"!"
                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                    binding.email.editText?.setText("")
                                    binding.password.editText?.setText("")
                                    binding.newUser.editText?.setText("")
                                })

                    }
                } else {
                    // sign in failed
                    Toast.makeText(context,"Invalid login information.",Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.returnFromEmail.setOnClickListener(View.OnClickListener {
            resetUI()
        })

        binding.returnFromPassword.setOnClickListener(View.OnClickListener {
            resetUI()
        })

        binding.returnFromUser.setOnClickListener(View.OnClickListener {
            resetUI()
        })


            return binding.root
        }

        fun hideAllUI(){
            binding.email.editText?.setText("")
            binding.password.editText?.setText("")
            binding.newEmail.editText?.setText("")
            binding.newUser.editText?.setText("")
            binding.newPassword.editText?.setText("")
            binding.email.visibility = View.INVISIBLE
            binding.password.visibility = View.INVISIBLE
            binding.newEmail.visibility = View.INVISIBLE
            binding.newPassword.visibility = View.INVISIBLE
            binding.enterNewEmail.visibility = View.INVISIBLE
            binding.submitEmailChange.visibility = View.INVISIBLE
            binding.enterNewPassword.visibility = View.INVISIBLE
            binding.returnFromEmail.visibility = View.INVISIBLE
            binding.returnFromPassword.visibility = View.INVISIBLE
            binding.submitPasswordChange.visibility = View.INVISIBLE
            binding.newUser.visibility = View.INVISIBLE
            binding.enterNewUser.visibility = View.INVISIBLE
            binding.returnFromUser.visibility = View.INVISIBLE
            binding.submitUserChange.visibility = View.INVISIBLE
            binding.changeEmail.visibility = View.INVISIBLE
            binding.helloUser.visibility = View.INVISIBLE
            binding.blank1.visibility = View.INVISIBLE
            binding.blank2.visibility = View.INVISIBLE
            binding.blank3.visibility = View.INVISIBLE
            binding.blank4.visibility = View.INVISIBLE
            binding.changePassword.visibility = View.INVISIBLE
            binding.changeUser.visibility = View.INVISIBLE
            binding.viewBlocklist.visibility = View.INVISIBLE

        }


        fun resetUI(){
            hideAllUI()
            binding.changeEmail.visibility = View.VISIBLE
            binding.helloUser.visibility = View.VISIBLE
            binding.blank1.visibility = View.VISIBLE
            binding.blank2.visibility = View.VISIBLE
            binding.blank3.visibility = View.VISIBLE
            binding.blank4.visibility = View.VISIBLE
            binding.changePassword.visibility = View.VISIBLE
            binding.changeUser.visibility = View.VISIBLE
            binding.viewBlocklist.visibility = View.VISIBLE

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


