package com.abing.letak.sampleadapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.databinding.UserVehicleItemBinding
import com.abing.letak.managevehicle.ManageVehicleActivity
import com.abing.letak.model.Vehicle

class VehicleAdapter(val dataset: MutableList<Vehicle>, val context: Context): RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = UserVehicleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        Log.d("vehicle adapter", "${dataset.size}")
        val vehicles = dataset[position]
        holder.binding.userVehicleName.text = vehicles.vecName
        holder.binding.userVehiclePlate.text = vehicles.vecPlate
        holder.binding.vehicleColor.text = vehicles.vecColour

        holder.binding.manageVehicleButton.setOnClickListener {
            val intent = Intent(context, ManageVehicleActivity::class.java)
            intent.putExtra("vecPlate", vehicles.vecPlate)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class VehicleViewHolder(val binding: UserVehicleItemBinding): RecyclerView.ViewHolder(binding.root){

    }
}