package com.example.mycharge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mycharge.network.MyForms

class FormAdapter(val mylist:List<MyForms>): RecyclerView.Adapter<FormAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val start = itemView.findViewById<TextView>(R.id.start_item)
        val consume = itemView.findViewById<TextView>(R.id.consume_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.form_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.start.text = mylist[position].start
        val consumeTime = mylist[position].consume.toLong()
        if(consumeTime>60){
            val consumeMinutes = consumeTime/60
            holder.consume.text = "已支付时长: $consumeTime 分"
        }else{

            holder.consume.text = "已支付时长: $consumeTime 秒"
        }
    }

    override fun getItemCount() = mylist.size
}