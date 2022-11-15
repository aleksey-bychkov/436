package com.example.loginwithfirebase

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loginwithfirebase.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
private lateinit var binding: FragmentLoginBinding

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.login.setOnClickListener { loginUserAccount() }
        return binding.root
    }

    private fun loginUserAccount(){
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()

        findNavController().navigate(
            R.id.action_loginFragment_to_dashboardFragment
        )

    }

}