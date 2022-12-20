package com.abing.letak.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ParkingFeeViewModel: ViewModel() {

    val parkingFee: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }


    fun calculateFee(spaceType: String, hour: String, minute: String){
        parkingFee.value = 0.0
        when (spaceType) {
            "Green" -> {
                var totalFee = hour.toDouble() * 0.5
                if (minute.toDouble() > 0.0){
                    totalFee += 0.5
                    parkingFee.value = totalFee
                }else {
                    parkingFee.value = totalFee
                }
            }
            "Yellow" -> {
                val totalMinutes = (hour.toInt()*60) + minute.toInt()
                if (totalMinutes <= 240){
                    val totalSegment = totalMinutes / 30
                    var parkingFeeSubtotal = totalSegment.toDouble() * 0.5

                    if (totalMinutes % 30 != 0){
                        parkingFeeSubtotal += 0.5
                    }
                    parkingFee.value = parkingFeeSubtotal
                }else {
                    val totalNewSegment = (totalMinutes - 240) / 30
                    var parkingFeeSubtotal = (totalNewSegment.toDouble() * 1) + 4

                    if ((totalMinutes - 240) % 30 != 0){
                        parkingFeeSubtotal += 1.0
                    }
                    parkingFee.value = parkingFeeSubtotal
                }
            }
            "Red" -> {
                val totalMinutes = (hour.toInt()*60) + minute.toInt()
                if (totalMinutes <= 60){
                    val totalSegment = totalMinutes / 30
                    var parkingFeeSubtotal = totalSegment.toDouble() * 1.0

                    if (totalMinutes % 30 != 0){
                        parkingFeeSubtotal += 1.0
                    }
                    parkingFee.value = parkingFeeSubtotal
                }else {
                    val totalNewSegment  = (totalMinutes - 60) / 30
                    var parkingFeeSubtotal = (totalNewSegment.toDouble() * 2) + 2

                    if ((totalMinutes - 60) % 30 != 0){
                        parkingFeeSubtotal += 2
                    }
                    parkingFee.value = parkingFeeSubtotal

                }
            }
        }
    }
}