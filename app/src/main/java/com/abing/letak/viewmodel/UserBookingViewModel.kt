package com.abing.letak.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abing.letak.model.User
import com.abing.letak.model.UserBooking

class UserBookingViewModel: ViewModel() {

    private val _bookingId = MutableLiveData<String>()
    val bookingId: LiveData<String> = _bookingId

    private val _lotId = MutableLiveData<String>()
    val lotId: LiveData<String> = _lotId

    private val _spaceId = MutableLiveData<String>()
    val spaceId: LiveData<String> = _spaceId

    private val _spaceType = MutableLiveData<String>()
    val spaceType: LiveData<String> = _spaceType

    private val _parkingPeriodMinute = MutableLiveData<Int>()
    val parkingPeriodMinute: LiveData<Int> = _parkingPeriodMinute

    private val _parkingStart = MutableLiveData<String>()
    val parkingStart: LiveData<String> = _parkingStart

    private val _parkingEnd = MutableLiveData<String>()
    val parkingEnd: LiveData<String> = _parkingEnd

    private val _eWalletType = MutableLiveData<String>()
    val eWalletType: LiveData<String> = _eWalletType

    private val _vecPlate = MutableLiveData<String>()
    val vecPlate: LiveData<String> = _vecPlate

    init {
        reset()
    }

    fun setSpaceId(spaceId: String){
        _spaceId.value = spaceId
    }

    private fun reset(){
        _lotId.value = ""
        _parkingPeriodMinute.value = 0
        _spaceType.value = ""
    }

    fun setLotId(lotId: String){
        _lotId.value = lotId
    }

    fun setParkingPeriodMinute(parkingPeriod: Int){
        _parkingPeriodMinute.value = parkingPeriod
    }

    fun setSpaceType(spaceType: String){
        _spaceType.value = spaceType
    }

    fun setEWalletType(eWalletType: String){
        _eWalletType.value = eWalletType
    }

    fun setVecPlate(vehicleId: String){
        _vecPlate.value = vehicleId
    }
}