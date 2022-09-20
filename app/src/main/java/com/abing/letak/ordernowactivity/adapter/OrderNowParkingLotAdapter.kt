package com.abing.letak.ordernowactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abing.letak.databinding.AvailableParkingLotItemBinding
import com.abing.letak.model.ParkingLot

class OrderNowParkingLotAdapter(private val context: Context, private val dataset: List<ParkingLot>): RecyclerView.Adapter<
        OrderNowParkingLotAdapter.OrderNowParkingLotViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderNowParkingLotViewHolder {
        val binding = AvailableParkingLotItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return return OrderNowParkingLotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderNowParkingLotViewHolder, position: Int) {
        val parkingLot = dataset[position]
        holder.binding.parkingLotName.text = parkingLot.parkingLotName
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class OrderNowParkingLotViewHolder(val binding: AvailableParkingLotItemBinding):
            RecyclerView.ViewHolder(binding.root){}
}