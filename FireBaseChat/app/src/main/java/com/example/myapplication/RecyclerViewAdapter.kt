package com.example.myapplication

import android.content.Context
import android.graphics.Color.parseColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class RecyclerViewAdapter
    (private var context: Context, private var list: ArrayList<Message>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var username: TextView = itemView.findViewById(R.id.username)
        var left: ConstraintLayout = itemView.findViewById(R.id.left)
        var cardL: CardView = itemView.findViewById(R.id.cardLeft)
        var messageL: TextView = itemView.findViewById(R.id.user_message_left)
        var dateTimeL: TextView = itemView.findViewById(R.id.user_message_date_time_left)
        var messageIDL:TextView = itemView.findViewById(R.id.messageID_left)
        var reportButtonL: FloatingActionButton = itemView.findViewById(R.id.reportButton_left)

        var right: ConstraintLayout = itemView.findViewById(R.id.right)
        var cardR: CardView = itemView.findViewById(R.id.cardRight)
        var messageR: TextView = itemView.findViewById(R.id.user_message_right)
        var dateTimeR: TextView = itemView.findViewById(R.id.user_message_date_time_right)
        var messageIDR:TextView = itemView.findViewById(R.id.messageID_right)
        var reportButtonR: FloatingActionButton = itemView.findViewById(R.id.reportButton_right)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.messages,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.username.text = list.get(position).getUserEmail()

        if(FirebaseAuth.getInstance().uid == list.get(position).getUserID()){
            holder.right.visibility = View.VISIBLE
            holder.messageR.text = list.get(position).getMsg()
            holder.dateTimeR.text = list.get(position).getDateTime()
            holder.messageIDR.text = list.get(position).getMessageID()
            holder.cardR.setCardBackgroundColor(parseColor("#CE93D8"))
            holder.reportButtonR.visibility = View.GONE
            holder.left.visibility = View.GONE

        } else {
            holder.left.visibility = View.VISIBLE
            holder.messageL.text = list.get(position).getMsg()
            holder.dateTimeL.text = list.get(position).getDateTime()
            holder.messageIDL.text = list.get(position).getMessageID()
            holder.cardL.setCardBackgroundColor(parseColor("#D3D3D3"))
            holder.reportButtonL.visibility = View.VISIBLE
            holder.right.visibility = View.GONE

        }


    }

    override fun getItemCount(): Int {
        return this.list.size
    }


}