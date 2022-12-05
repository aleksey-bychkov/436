package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentQuestionBinding
import com.google.firebase.auth.FirebaseAuth

class QuestionFragment(topicID: String) : Fragment() {

    private var topicID = topicID
    private var responselist = ArrayList<String>()
    private var questionNumber = 1
    private lateinit var binding: FragmentQuestionBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()

        binding.questionheader.text = "Question " + questionNumber + " of 5"
        binding.questionText.text = getString("topic" + topicID + "question" + questionNumber)

        binding.option1.setOnClickListener{
            nextQuestion("1")
        }
        binding.option2.setOnClickListener{
            nextQuestion("2")
        }
        binding.option3.setOnClickListener{
            nextQuestion("3")
        }
        binding.option4.setOnClickListener{
            nextQuestion("4")
        }
        binding.option5.setOnClickListener{
            nextQuestion("5")
        }
        return binding.root
    }

    private fun getString(aString: String): String? {
        val packageName= getActivity()?.getPackageName()
        val resId = resources.getIdentifier(aString, "string", packageName)
        return getString(resId)
    }

    private fun saveSurvey()
    {
        // save to DB
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(this.id, SurveyResultsFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    private fun nextQuestion(value: String)
    {
        responselist.add(value)
        questionNumber++

        if (questionNumber > 5)
            saveSurvey()
        else {
            // make call to DB
            binding.questionheader.text = "Question " + questionNumber + " of 5"
            binding.questionText.text = getString("topic" + topicID + "question" + questionNumber)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}


