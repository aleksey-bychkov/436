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
    var list = ArrayList<Contacts>()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mAuth: FirebaseAuth
    lateinit var adapter: RecyclerViewBlocklist
    lateinit var listener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun receiveContacts(){
        listener = db.child("Users").child(mAuth.currentUser?.uid!!).child("Contacts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                list.clear()
                for (snap in snapshot.children){

                    val contact = snap.getValue(Contacts::class.java)

                    if (contact != null) {
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
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        adapter = context?.let { RecyclerViewBlocklist(it, list) }!!
        val llm: LinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.blocklistRecycler.layoutManager = llm
        binding.blocklistRecycler.adapter = adapter

        db.child("Users").child(mAuth.currentUser!!.uid).get().addOnCompleteListener(){
            Log.i("test",it.result.child("Username").value.toString())
            binding.helloUser.text = getString(R.string.hello_settings)+" "+it.result.child("Username").value.toString()+"!"
            receiveContacts()

        }

        binding.viewBlocklist.setOnClickListener(View.OnClickListener {
            hideAllUI()
            binding.blocklistRecycler.visibility = View.VISIBLE
            binding.returnBlock.visibility = View.VISIBLE
            binding.layout.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        })

        binding.changeEmail.setOnClickListener(View.OnClickListener {
            hideAllUI()
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

        binding.changePassword.setOnClickListener(View.OnClickListener {
            hideAllUI()
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

        binding.changeUser.setOnClickListener(View.OnClickListener {
            hideAllUI()
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

        binding.submitEmailChange.setOnClickListener(View.OnClickListener {
            mAuth.signInWithEmailAndPassword(binding.email.editText!!.text.toString(),binding.password.editText!!.text.toString()).addOnCompleteListener(){ task ->
                    if (task.isSuccessful) {
                        // Sign in success now update email
                        if(binding.newEmail.editText!!.text.toString() == binding.confirmEmail.editText!!.text.toString()){
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
                            Toast.makeText(context, "The emails you entered do not match!",Toast.LENGTH_SHORT).show()
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
                    // Sign in success now update password
                    if(binding.newPassword.editText!!.text.toString() == binding.confirmPassword.editText!!.text.toString()){
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
                        Toast.makeText(context,"The passwords you entered don't match.",Toast.LENGTH_SHORT).show()
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
                        if(binding.newUser.editText!!.text.toString() == binding.confirmUser.editText!!.text.toString()){
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
                        } else {
                            Toast.makeText(context,"The usernames you entered don't match.",Toast.LENGTH_SHORT).show()
                        }


                    }
                } else {
                    // sign in failed
                    Toast.makeText(context,"Invalid login information.",Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.returnHome.setOnClickListener(View.OnClickListener {
            resetUI()
        })
        binding.returnBlock.setOnClickListener(View.OnClickListener {
            resetUI()
            binding.layout.gravity = Gravity.CENTER
        })

        binding.signOut.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
            startActivity(Intent(context, LoginRegistration::class.java))
        })



            return binding.root
        }

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
        db.child("Users").child(mAuth.currentUser?.uid!!).child("Contacts").removeEventListener(listener)
    }

    }


