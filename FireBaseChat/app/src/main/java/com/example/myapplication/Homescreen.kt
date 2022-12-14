package com.example.myapplication

import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.databinding.HomescreenBinding

internal class Homescreen : AppCompatActivity() {
    private lateinit var binding: HomescreenBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate view
        binding = HomescreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // current fragment home fragment
        setCurrentFragment(HomeFragment())
        // set listener for fragment change
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                // case switches for each fragment
                R.id.home->setCurrentFragment(HomeFragment())
                R.id.settings->setCurrentFragment(SettingsFragment())
                R.id.messages->setCurrentFragment(MessagesPreview())
                R.id.survey->setCurrentFragment(SurveyFragment())
            }

            return@setOnItemSelectedListener true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        // fragment transaction to switch fragments
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.currentState, fragment)
            commit()
        }

    }


}