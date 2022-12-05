package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentSurveyResultsBinding
import com.google.firebase.auth.FirebaseAuth

class SurveyResultsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyResultsBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentSurveyResultsBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()

        binding.person1.setOnClickListener{
            startConversation("1")
        }
        binding.person2.setOnClickListener{
            startConversation("2")
        }
        binding.person3.setOnClickListener{
            startConversation("3")
        }
        binding.person4.setOnClickListener{
            startConversation("4")
        }

        return binding.root
    }

    private fun startConversation( topicId: String)
    {
        // figure this out
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}


