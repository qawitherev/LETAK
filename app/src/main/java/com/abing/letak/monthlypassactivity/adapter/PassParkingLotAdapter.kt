package com.abing.letak.monthlypassactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.databinding.AvailableParkingLotItemBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.monthlypassactivity.fragments.MonthlyPassParkingLotSelectionFragmentDirections

class PassParkingLotAdapter(
    private val context: Context,
    private val dataset: List<ParkingLot>
) : RecyclerView.Adapter<PassParkingLotAdapter.PassParkingLotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassParkingLotViewHolder {
        val binding = AvailableParkingLotItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PassParkingLotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PassParkingLotViewHolder, position: Int) {
        val parkingLot = dataset[position]
        holder.binding.parkingLotName.text = parkingLot.parkingLotName
        holder.binding.selectedParkingLot.setOnClickListener { continueToPassSelection(it) }
    }

    private fun continueToPassSelection(view: View?) {
        val action = MonthlyPassParkingLotSelectionFragmentDirections
            .actionMonthlyPassParkingLotSelectionFragmentToMonthlyPassSpaceSelection()
        view?.findNavController()?.navigate(action)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class PassParkingLotViewHolder(val binding: AvailableParkingLotItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}