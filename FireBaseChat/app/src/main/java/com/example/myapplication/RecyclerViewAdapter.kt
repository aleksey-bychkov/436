package com.example.myapplication

import android.content.Context
import android.graphics.Color.parseColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class RecyclerViewAdapter
    (private var context: Context, private var list: ArrayList<Message>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var username: TextView = itemView.findViewById(R.id.username)
        var message: TextView = itemView.findViewById(R.id.user_message)
        var dateTime: TextView = itemView.findViewById(R.id.user_message_date_time)
        var messageID:TextView = itemView.findViewById(R.id.messageID)
        var reportButton: FloatingActionButton = itemView.findViewById(R.id.reportButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.messages,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.username.text = list.get(position).getUserEmail()
        holder.message.text = list.get(position).getMsg()
        holder.dateTime.text = list.get(position).getDateTime()
        holder.messageID.text = list.get(position).getMessageID()
        if(FirebaseAuth.getInstance().uid == list.get(position).getUserID()){
            holder.itemView.setBackgroundColor(parseColor("#CE93D8"))
            holder.reportButton.visibility = View.INVISIBLE

        } else {
            holder.itemView.setBackgroundColor(parseColor("#D3D3D3"))
        }


    }

    override fun getItemCount(): Int {
        return this.list.size
    }


}