package com.example.edward.rxjavaroomexercise

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.user_item.view.*

/**
 * Created by Edward on 1/13/2019.
 */
class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    private val userList = arrayListOf<User>()

    fun setList(list: List<User>){
        userList.addAll(list)
    }

    fun showUser(list: List<User>){
        userList.clear()
        userList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)

        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int) {
        with(viewHolder){
            nameView.text = userList[position].name
            contactView.text = userList[position].contact
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameView: TextView = itemView.textUserName
        val contactView: TextView = itemView.textContact
    }
}
