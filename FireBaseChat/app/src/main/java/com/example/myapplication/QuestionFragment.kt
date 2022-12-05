package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentQuestionBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class QuestionFragment(topicID: String) : Fragment() {

    private var topicID = topicID
    private var responselist = ArrayList<Int>()
    private var questionNumber = 1
    private lateinit var binding: FragmentQuestionBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        db = FirebaseDatabase.getInstance().reference
        // Inflate the layout for this fragment
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()

        binding.questionheader.text = getString("question" + questionNumber)
        binding.questionText.text = getString("topic" + topicID + "question" + questionNumber)

        binding.option1.setOnClickListener{
            nextQuestion(1)
        }
        binding.option2.setOnClickListener{
            nextQuestion(2)
        }
        binding.option3.setOnClickListener{
            nextQuestion(3)
        }
        binding.option4.setOnClickListener{
            nextQuestion(4)
        }
        binding.option5.setOnClickListener{
            nextQuestion(5)
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
        val agg : Float = (responselist.get(0) + responselist.get(1) + responselist.get(2) + responselist.get(3) + responselist.get(4))/5.0F
        db.child("Views").child(mAuth.currentUser!!.uid).child(topicID).setValue(ViewResponse(mAuth.currentUser!!.uid, responselist.get(0), responselist.get(1), responselist.get(2), responselist.get(3), responselist.get(4), agg.toFloat())).addOnCompleteListener(
            OnCompleteListener {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.replace(this.id, SurveyResultsFragment(topicID,agg))
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }
        })
    }

    private fun nextQuestion(value: Int)
    {
        responselist.add(value)
        questionNumber++

        if (questionNumber > 5)
            saveSurvey()
        else {
            binding.questionheader.text = getString("question" + questionNumber)
            binding.questionText.text = getString("topic" + topicID + "question" + questionNumber)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}


