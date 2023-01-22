package com.abing.letak.extendparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.ActivityExtendParkingBinding
import com.abing.letak.utils.MinMaxFilter
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen
import com.abing.letak.viewmodel.ParkingFeeViewModel
import com.abing.letak.viewmodel.UserBookingViewModel
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExtendParkingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExtendParkingBinding
    private var durationHour: Long = 0
    private var durationMinute: Long = 0
    private val db = FirebaseFirestore.getInstance()
    private val userIdViewModel: UserIdViewModel by viewModels()
    private var activeBookingId: String? = null
    private lateinit var bookingRef: DocumentReference
    private val TAG = "ExtendParkingActivity"
    private val parkingFeeViewModel: ParkingFeeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtendParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lightStatusBar(window, false, false)

        initView()
        initData()
        buttonListeners()
    }

    private fun initData() {
        //get booking reference
        activeBookingId = intent.getStringExtra("activeBookingId")
        val userId = userIdViewModel.userId
        bookingRef = db.collection("users").document(userId).collection("bookings").document(activeBookingId.toString())
    }

    private fun buttonListeners() {
        binding.apply {
            cancelButton.setOnClickListener{
                finish()
            }
            extendButton.setOnClickListener{
                extendParking()
            }
        }
    }

    private fun extendParking() {
        if (isHourEmpty()){
            binding.durationHour.setText("0")
        }
        if (isMinuteEmpty()){
            binding.durationMinute.setText("0")
        }
        durationHour = binding.durationHour.text.toString().toInt().toLong()
        durationMinute = binding.durationMinute.text.toString().toInt().toLong()
        bookingRef.get()
            .addOnSuccessListener {
                val parkingPeriodMinute = it.get("parkingPeriodMinute").toString().toInt()
                val newParkingPeriodMinute = parkingPeriodMinute + (durationHour * 60).toInt() + durationMinute.toInt()
                val durationHour = java.time.Duration.ofHours(durationHour)
                val durationMinute = java.time.Duration.ofMinutes(durationMinute)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val currentTime = LocalDateTime.now()
                val endTime = currentTime.plus(durationHour).plus(durationMinute).format(formatter)
                val feePaid = it.get("feePaid").toString()
                val spaceType = it.getString("spaceType").toString()
                if (spaceType == "Red" && redParkingInvalid(newParkingPeriodMinute)){
                    Toast.makeText(this, R.string.red_valid_below_two_hours, Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                calculateExtendFee(spaceType, newParkingPeriodMinute, feePaid.toDouble())
                //update periodParkingMinute and parkingEnd in Firestore
                bookingRef.update("parkingPeriodMinute", newParkingPeriodMinute,
                "parkingEnd", endTime)
                finish()
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            }
    }

    private fun redParkingInvalid(parkingPeriod: Int): Boolean {
        val totalMinute = binding.durationHour.text.toString().toInt() +
                binding.durationMinute.text.toString().toInt()
        Log.d("SpaceSelectionFragment", "total minute is -> $totalMinute")
        return totalMinute >= 120
    }

    private fun calculateExtendFee(spaceType: String, newParkingPeriodMinute: Int, feePaid: Double) {
        //parkingFeeViewModel accepts spaceType, hour and minute all strings
        val hour = newParkingPeriodMinute.div(60).toString()
        val minute = newParkingPeriodMinute.mod(60).toString()
        parkingFeeViewModel.calculateFee(spaceType, hour, minute)
        //extend fee = feeAll - feePaid
        parkingFeeViewModel.calculateExtend(feePaid)
        val extendFee = parkingFeeViewModel.extendFee.value?.toDouble()
        parkingFeeViewModel.extendFee.observe(this, androidx.lifecycle.Observer {
            binding.extendCharge.text = it.toString()
        })
    }

    private fun isMinuteEmpty(): Boolean {
        return binding.durationMinute.text.isBlank()
    }

    private fun isHourEmpty(): Boolean {
        return binding.durationHour.text.isBlank()
    }

    private fun initView() {
        binding.durationHour.setText("1")
        binding.durationMinute.setText("30")
        binding.durationHour.filters = arrayOf<InputFilter>(MinMaxFilter(0, 23))
        binding.durationMinute.filters = arrayOf<InputFilter>(MinMaxFilter(0, 59))
    }
}