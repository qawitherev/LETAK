package com.abing.letak.sampleadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.databinding.UserVehicleItemBinding
import com.abing.letak.model.Vehicle

class VehicleAdapter(val dataset: List<Vehicle>): RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = UserVehicleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = dataset[position]
        holder.binding.userVehicleName.text = vehicle.vehicleName
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class VehicleViewHolder(val binding: UserVehicleItemBinding): RecyclerView.ViewHolder(binding.root){

    }
}