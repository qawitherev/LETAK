package com.abing.letak.sampleadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.databinding.NotificationItemBinding
import com.abing.letak.model.Notification

class NotificationAdapter(val dataset: List<Notification>): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){
    class NotificationViewHolder(val binding: NotificationItemBinding): RecyclerView.ViewHolder(binding.root){
        // TODO: nothing to do here???
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = dataset[position]
        holder.binding.notificationItem.text = notification.notificationName
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}