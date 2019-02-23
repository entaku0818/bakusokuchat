package com.entaku.bakusokuchat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.entaku.bakusokuchat.Message
import com.entaku.bakusokuchat.R
import com.entaku.bakusokuchat.databinding.ItemMessageBinding
import com.squareup.picasso.Picasso

class MessageAdapter(private val list:List<Message>):RecyclerView.Adapter<MessageAdapter.MessageViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflate =LayoutInflater.from(parent.context)

        return MessageViewHolder(DataBindingUtil.inflate(inflate, R.layout.item_message,parent,false))

    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.binding.apply {
            message=list[position]
            Picasso.get().load(list[position].profileUrl).into(holder.binding.imageView)

        }
    }

    inner class MessageViewHolder(val binding:ItemMessageBinding):RecyclerView.ViewHolder(binding.root)
}