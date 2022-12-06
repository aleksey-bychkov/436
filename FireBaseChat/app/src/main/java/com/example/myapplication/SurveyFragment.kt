package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentSurveyBinding
import com.google.firebase.auth.FirebaseAuth

class SurveyFragment : Fragment() {
    private lateinit var binding: FragmentSurveyBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // inflate view
        binding = FragmentSurveyBinding.inflate(inflater, container, false)
        // set auth
        mAuth = FirebaseAuth.getInstance()
        // start question from click
        binding.option1.setOnClickListener{
            startQuestions("1")
        }
        binding.option2.setOnClickListener{
            startQuestions("2")
        }
        binding.option3.setOnClickListener{
            startQuestions("3")
        }
        binding.option4.setOnClickListener{
            startQuestions("4")
        }
        binding.option5.setOnClickListener{
            startQuestions("5")
        }

        return binding.root
    }

    private fun startQuestions( topicId: String)
    {
        // switch fragment
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(this.id, QuestionFragment(topicId))
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}


