package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter
    (private var context: Context, private var list: ArrayList<Message>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.user_email)
        var message: TextView = itemView.findViewById(R.id.user_message)
        var dateTime: TextView = itemView.findViewById(R.id.user_message_date_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.messages,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = list.get(position).getUserEmail()
        holder.message.text = list.get(position).getMsg()
        holder.dateTime.text = list.get(position).getDateTime()
    }

    override fun getItemCount(): Int {
        return this.list.size
    }


}