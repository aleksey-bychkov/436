package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RecyclerViewBlocklist (private var context: Context, private var list: ArrayList<Contacts>) : RecyclerView.Adapter<RecyclerViewBlocklist.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // get relevant layout pieces
        var username: TextView = itemView.findViewById(R.id.username)
        var blocked: ImageView = itemView.findViewById(R.id.block)
        var unblocked: ImageView = itemView.findViewById(R.id.unblock)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewBlocklist.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.blocklist,parent,false)
        return RecyclerViewBlocklist.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewBlocklist.ViewHolder, position: Int) {
        // get current database and user
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseDatabase.getInstance().reference
        // set the username of the user
        holder.username.text = list.get(position).getUsername()
        // add listener to blocked icon to block a user
        holder.blocked.setOnClickListener(View.OnClickListener {
            // change user to blocked in contacts
            db.child("Users").child(user!!.uid).child("Contacts").child(list.get(position).getTargetUID()).child("isBlocked").setValue(true)
                .addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            // if successful say it succeeded and change the user to be displayed as blocked
                            Toast.makeText(context,holder.itemView.resources.getString(R.string.user_blocked),Toast.LENGTH_SHORT).show()
                            holder.blocked.visibility = View.GONE
                            holder.unblocked.visibility = View.VISIBLE
                        } else {
                            // if failed notify user
                            Toast.makeText(context,holder.itemView.resources.getString(R.string.user_blocked_failed),Toast.LENGTH_SHORT).show()
                        }

                    })
        })

        // add listener to blocked icon to block a user
        holder.unblocked.setOnClickListener(View.OnClickListener {
            // change user to unblocked in contacts
            db.child("Users").child(user!!.uid).child("Contacts").child(list.get(position).getTargetUID()).child("isBlocked").setValue(false)
                .addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            // if successful say it succeeded and change the user to be displayed as unblocked
                            Toast.makeText(context,holder.itemView.resources.getString(R.string.user_unblocked),Toast.LENGTH_SHORT).show()
                            holder.unblocked.visibility = View.GONE
                            holder.blocked.visibility = View.VISIBLE
                        } else {
                            // if failed notify user
                            Toast.makeText(context,holder.itemView.resources.getString(R.string.user_unblocked_failed),Toast.LENGTH_SHORT).show()
                        }

                    })
        })

        // check if the user is blocked or not and update UI accordingly
        if(!list.get(position).getIsBlocked()){
            holder.blocked.visibility = View.VISIBLE
            holder.unblocked.visibility = View.GONE

        } else {
            holder.unblocked.visibility = View.VISIBLE
            holder.blocked.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return this.list.size
    }


}