package com.abing.letak.advancebookingactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.advancebookingactivity.fragments.AdvanceLotSelectionFragmentDirections
import com.abing.letak.databinding.AvailableParkingLotItemBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.ordernowactivity.fragments.ParkingLotSelectionFragmentDirections

class AdvanceLotAdapter(
    private val context: Context,
    private val dataset: MutableList<ParkingLot>,
) : RecyclerView.Adapter<
        AdvanceLotAdapter.AdvanceLotViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdvanceLotViewHolder {
        val binding = AvailableParkingLotItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdvanceLotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvanceLotViewHolder, position: Int) {
        val parkingLot = dataset[position]
        holder.binding.parkingLotName.text = parkingLot.lotName
        holder.binding.selectedParkingLot.setOnClickListener {
            dataset.clear()
            val action = AdvanceLotSelectionFragmentDirections
                .actionAdvanceLotSelectionFragmentToAdvanceSpaceSelectionFragment(parkingLot.lotId!!)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class AdvanceLotViewHolder(val binding: AvailableParkingLotItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}