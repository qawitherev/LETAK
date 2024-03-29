package com.abing.letak.parkinglotadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.databinding.ParkingLotItemBinding
import com.abing.letak.model.ParkingLot

class ParkingLotAdapter(private val context: Context, private val dataset: MutableList<ParkingLot>) :
    RecyclerView.Adapter<ParkingLotAdapter.ParkingLotViewHolder>() {

    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingLotViewHolder {
        val binding =
            ParkingLotItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParkingLotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParkingLotViewHolder, position: Int) {
        val data = dataset[position]
        holder.binding.parkingLotName.text = data.lotName
        holder.binding.parkingLotOccupancyBar.max = data.lotTotal!!.toInt()
        holder.binding.parkingLotOccupancyBar.progress = data.lotOccupied!!.toInt()
        holder.binding.parkingLotOccupancy.text = "${data.lotOccupied} of ${data.lotTotal} occupied"
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ParkingLotViewHolder(val binding: ParkingLotItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // TODO: nothing here???
    }
}