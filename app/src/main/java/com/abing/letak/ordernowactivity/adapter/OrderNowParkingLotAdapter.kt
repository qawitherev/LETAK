package com.abing.letak.ordernowactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.databinding.AvailableParkingLotItemBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.ordernowactivity.fragments.ParkingLotSelectionFragmentDirections

class OrderNowParkingLotAdapter(
    private val context: Context,
    private val dataset: List<ParkingLot>
) : RecyclerView.Adapter<
        OrderNowParkingLotAdapter.OrderNowParkingLotViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderNowParkingLotViewHolder {
        val binding = AvailableParkingLotItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderNowParkingLotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderNowParkingLotViewHolder, position: Int) {
        val parkingLot = dataset[position]
        holder.binding.parkingLotName.text = parkingLot.parkingLotName
        holder.binding.selectedParkingLot.setOnClickListener {
            val action = ParkingLotSelectionFragmentDirections
                .actionParkingLotSelectionFragmentToSpaceSelectionFragment()
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class OrderNowParkingLotViewHolder(val binding: AvailableParkingLotItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}