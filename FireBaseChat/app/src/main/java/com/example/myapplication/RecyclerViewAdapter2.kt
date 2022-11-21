package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class RecyclerViewAdapter2 (private var context: Context, private var list: ArrayList<MessagePreview>) : RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var username: TextView = itemView.findViewById(R.id.username)
        var username: TextView = itemView.findViewById(R.id.username)
        var message: TextView = itemView.findViewById(R.id.messageID2)
        var dateTime: TextView = itemView.findViewById(R.id.user_message_date_time2)
        var messageID: TextView = itemView.findViewById(R.id.messageID2)
        var viewMessage: FloatingActionButton = itemView.findViewById(R.id.viewMessage)
        var targetID: TextView = itemView.findViewById(R.id.targetID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.message_preview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = list.get(position).getUsername()
        holder.message.text = list.get(position).getMsg()
        holder.dateTime.text = list.get(position).getDateTime()
        holder.messageID.text = ""
        holder.targetID.text = list.get(position).getTargetUID()
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("target",list.get(position).getTargetUID())
        holder.itemView.setOnClickListener(View.OnClickListener {
            startActivity(context, Intent(context, MainActivity::class.java), null)})
        
    }

    override fun getItemCount(): Int {
        return this.list.size
    }



}
