package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class RecyclerViewHome (private var context: Context, private var list: ArrayList<Contacts>) : RecyclerView.Adapter<RecyclerViewHome.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // set vars to each text view
        var username: TextView = itemView.findViewById(R.id.username)
        var message: TextView = itemView.findViewById(R.id.user_message2)
        var messageID: TextView = itemView.findViewById(R.id.messageID2)
        var viewMessage: FloatingActionButton = itemView.findViewById(R.id.viewMessage)
        var targetID: TextView = itemView.findViewById(R.id.targetID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder
        var view = LayoutInflater.from(context).inflate(R.layout.home_message_preview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(list.get(position).getMsgID() == "temp"){
            // prompt the user to start a conversation
            holder.message.text = holder.itemView.context.getString(R.string.start_convo)
        } else {
            // block message if reported
            if (list.get(position).getIsReported()) {
                holder.message.text = R.string.reported_message.toString()
            } else {
                holder.message.text = list.get(position).getMsg()
            }
        }
        // set appropriate values from list
        holder.username.text = list.get(position).getUsername()
        holder.messageID.text = list.get(position).getMsgID()
        holder.targetID.text = list.get(position).getTargetUID()

        // set listener to open message
        holder.itemView.setOnClickListener(View.OnClickListener {
            /// set message to read
            if(!list.get(position).getIsRead()){
                db.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .child("Contacts").child(list.get(position).getTargetUID()).child("isRead").setValue(true)
            }
            // start main activity
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("target",list.get(position).getTargetUID())
            startActivity(context, intent, null)})

    }

    override fun getItemCount(): Int {
        return this.list.size
    }



}
