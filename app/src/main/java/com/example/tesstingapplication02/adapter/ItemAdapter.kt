package com.example.tesstingapplication02.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tesstingapplication02.ItemPage
import com.example.tesstingapplication02.R

class ItemAdapter(var mutableList: ArrayList<ItemPage.Data>,val context:Context): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(v: View):RecyclerView.ViewHolder(v)
    {
             val txt_email:TextView = v.findViewById(R.id.item_layout_email)
               val txt_name:TextView = v.findViewById(R.id.item_layout_name)
        val image:ImageView = v.findViewById(R.id.profile_image)
        val txt_lastname=v.findViewById<TextView>(R.id.item_layout_lastname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ItemViewHolder(v)
    }

    override fun getItemCount(): Int {
       return  mutableList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val list =   mutableList.get(position)

        holder.txt_email.text = list.email
        holder.txt_name.text=list.firstName
        holder.txt_lastname.text=list.lastName
        Glide.with(context).load(list.avatar).into(holder.image)


    }
}