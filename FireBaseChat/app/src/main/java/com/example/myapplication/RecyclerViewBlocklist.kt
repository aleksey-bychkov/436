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
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseDatabase.getInstance().reference
        Log.i("debug",user!!.uid)
        holder.username.text = list.get(position).getUsername()
        holder.blocked.setOnClickListener(View.OnClickListener {
            db.child("Users").child(user!!.uid).child("Contacts").child(list.get(position).getTargetUID()).child("isBlocked").setValue(true)
                .addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(context,"User blocked",Toast.LENGTH_SHORT).show()
                            holder.blocked.visibility = View.GONE
                            holder.unblocked.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(context,"Failed to block user",Toast.LENGTH_SHORT).show()
                        }

                    })
        })

        holder.unblocked.setOnClickListener(View.OnClickListener {
            db.child("Users").child(user!!.uid).child("Contacts").child(list.get(position).getTargetUID()).child("isBlocked").setValue(false)
                .addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(context,"User unblocked",Toast.LENGTH_SHORT).show()
                            holder.unblocked.visibility = View.GONE
                            holder.blocked.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(context,"Failed to unblock user",Toast.LENGTH_SHORT).show()
                        }

                    })
        })

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