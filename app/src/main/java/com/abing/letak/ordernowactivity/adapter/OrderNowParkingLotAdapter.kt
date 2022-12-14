package com.abing.letak.ordernowactivity.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.databinding.AvailableParkingLotItemBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.model.UserBooking
import com.abing.letak.ordernowactivity.fragments.ParkingLotSelectionFragmentDirections
import com.abing.letak.viewmodel.UserBookingViewModel
import com.abing.letak.viewmodel.UserIdViewModel

class OrderNowParkingLotAdapter(
    private val context: Context,
    private val dataset: MutableList<ParkingLot>,
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
        holder.binding.parkingLotName.text = parkingLot.lotName
        holder.binding.selectedParkingLot.setOnClickListener {
            dataset.clear()
            val action = ParkingLotSelectionFragmentDirections
                .actionParkingLotSelectionFragmentToSpaceSelectionFragment(parkingLot.lotId!!)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class OrderNowParkingLotViewHolder(val binding: AvailableParkingLotItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}