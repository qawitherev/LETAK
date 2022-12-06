package com.abing.letak.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ParkingFeeViewModel: ViewModel() {

    val parkingFee: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }


    fun calculateFee(spaceType: String, hour: String, minute: String){
        val hourInt = hour.toInt()
        val minuteInt = minute.toInt()
        if (spaceType == "Green"){
            // TODO: need to do some revising here esp for when minute is under 30
            val durationMinute = (hourInt * 60) + minuteInt
            val hourFee = durationMinute / 30
            val minuteFee = durationMinute % 30
            if (minuteFee % 30 != 0){
                hourFee + 1
            }

            parkingFee.value = hourFee.toDouble() * 0.5
        }
        // TODO: do for red and yellow


    }
}