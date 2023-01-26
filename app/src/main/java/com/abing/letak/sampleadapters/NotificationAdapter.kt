package com.abing.letak.sampleadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.R
import com.abing.letak.databinding.NotificationItemBinding
import com.abing.letak.model.Notification
import com.abing.letak.model.UserBooking

class NotificationAdapter(val context: Context, val dataset: ArrayList<UserBooking>): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){
    class NotificationViewHolder(val binding: NotificationItemBinding): RecyclerView.ViewHolder(binding.root){
        // TODO: nothing to do here???
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val booking = dataset[position]
        parkingLotName(booking)
        holder.binding.parkingLotName.text = parkingLotName(booking)
        holder.binding.vecPlate.text = booking.vecPlate
        holder.binding.parkingPeriod.text = parkingPeriod(booking.parkingPeriodMinute)
        when (booking.spaceType){
            "Green" -> {
                holder.binding.spaceTypeCard.backgroundTintList =
                    context.resources.getColorStateList(R.color.space_green)
            }
            "Yellow" -> {
                holder.binding.spaceTypeCard.backgroundTintList =
                    context.resources.getColorStateList(R.color.space_yellow)
            }
            "Red" -> {
                holder.binding.spaceTypeCard.backgroundTintList =
                    context.resources.getColorStateList(R.color.space_red)
            }
        }

    }

    private fun parkingPeriod(parkingPeriodMinute: Int?): String {
        val hour = parkingPeriodMinute!!.toInt().div(60)
        val minute = parkingPeriodMinute!!.toInt().rem(60)
        return "$hour hour and $minute minute"
    }

    private fun parkingLotName(booking: UserBooking): String {
        val lotId = booking.lotId
        when (lotId) {
            "ETStI0XcDkO8HKvtqAXH" -> {
                return "Gaya Street"
            }
            else -> {
                return "Unknown"
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}