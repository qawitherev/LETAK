package com.abing.letak.monthlypassactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.advancebookingactivity.adapter.AdvanceLotAdapter
import com.abing.letak.databinding.AvailableParkingLotItemBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.monthlypassactivity.fragments.MonthlyPassParkingLotSelectionFragmentDirections

class PassLotAdapter(
    private val context: Context,
    private val data: ArrayList<ParkingLot>
) : RecyclerView.Adapter<PassLotAdapter.PassLotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassLotViewHolder {
        val binding = AvailableParkingLotItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return PassLotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PassLotViewHolder, position: Int) {
        val parkingLot = data[position]
        holder.binding.parkingLotName.text = parkingLot.lotName
        holder.binding.selectedParkingLot.setOnClickListener {
            data.clear()
            val action = MonthlyPassParkingLotSelectionFragmentDirections
                .actionMonthlyPassParkingLotSelectionFragmentToMonthlyPassSpaceSelection(parkingLot.lotId!!)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class PassLotViewHolder(val binding: AvailableParkingLotItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}