package com.example.myapplication

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSurveys (private var context: Context, private var survList: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewSurveys.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // set to appropriate text view
        var surveyName: TextView = itemView.findViewById(R.id.surveyID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.home_survey_preview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display survey name retrieved by ID
        var topicID = survList.get(position)
        if (topicID == "1") {
            holder.surveyName.text = holder.itemView.resources.getString(R.string.topic1)
        } else if (topicID == "2") {
            holder.surveyName.text = holder.itemView.resources.getString(R.string.topic2)
        } else if (topicID == "3") {
            holder.surveyName.text = holder.itemView.resources.getString(R.string.topic3)
        } else if (topicID == "4") {
            holder.surveyName.text = holder.itemView.resources.getString(R.string.topic4)
        } else if (topicID == "5") {
            holder.surveyName.text = holder.itemView.resources.getString(R.string.topic5)
        }

    }


    override fun getItemCount(): Int {
        return this.survList.size
    }



}
