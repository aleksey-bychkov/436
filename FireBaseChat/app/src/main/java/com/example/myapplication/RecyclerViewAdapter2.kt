package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class RecyclerViewAdapter2 (private var context: Context, private var list: ArrayList<Contacts>) : RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var username: TextView = itemView.findViewById(R.id.username)
        var username: TextView = itemView.findViewById(R.id.username)
        var message: TextView = itemView.findViewById(R.id.user_message2)
        var dateTime: TextView = itemView.findViewById(R.id.user_message_date_time2)
        var messageID: TextView = itemView.findViewById(R.id.messageID2)
        var viewMessage: FloatingActionButton = itemView.findViewById(R.id.viewMessage)
        var targetID: TextView = itemView.findViewById(R.id.targetID)
        var read: ImageView = itemView.findViewById(R.id.read)
        var unread: ImageView = itemView.findViewById(R.id.unread)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.message_preview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(list.get(position).getMsgID() == "temp"){
            holder.message.text = "Start a conversation!"

        } else {
            if(list.get(position).getIsReported()){
                holder.message.text = ""
            } else {
                holder.message.text = list.get(position).getMsg()
            }
            holder.dateTime.text = list.get(position).getDateTime()
            holder.messageID.text = list.get(position).getMsgID()
        }

        if(list.get(position).getIsRead()){
            holder.read.visibility == View.VISIBLE
            holder.unread.visibility == View.INVISIBLE
        } else {
            holder.read.visibility == View.INVISIBLE
            holder.unread.visibility == View.VISIBLE
        }

        holder.username.text = list.get(position).getUsername()
        holder.targetID.text = list.get(position).getTargetUID()

        holder.itemView.setOnClickListener(View.OnClickListener {
            if(!list.get(position).getIsRead()){
                db.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .child("Contacts").child(list.get(position).getTargetUID()).child("isRead").setValue(true)
            }
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("target",list.get(position).getTargetUID())
            startActivity(context, intent, null)})

    }

    override fun getItemCount(): Int {
        return this.list.size
    }



}
