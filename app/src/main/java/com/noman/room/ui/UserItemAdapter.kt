package com.noman.room.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.noman.room.MainActivity
import com.noman.room.R
import com.noman.room.model.User

/**
 * User adapter class that is used to show user's data in UI
 */
class UserItemAdapter(var userList: List<User>, var itemSelectListener: MainActivity.OnItemSelectListener) : RecyclerView.Adapter<UserItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.view_user_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = userList.get(position).name
        holder.number.text = userList.get(position).number
        holder.email.text = userList.get(position).email

        holder.itemView.setOnClickListener {
            itemSelectListener.onItemSelect(userList.get(position))
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var number: TextView
        var email: TextView

        init {
            name = itemView?.findViewById(R.id.name)!!
            number = itemView?.findViewById(R.id.number)
            email = itemView?.findViewById(R.id.email)
        }
    }

}