package com.abing.letak.advancebookingactivity.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.abing.letak.R
import com.abing.letak.advancebookingactivity.viewmodels.AdvBookingViewModel
import com.abing.letak.databinding.FragmentAdvanceSpaceSelectionFragmentBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.ordernowactivity.adapter.SpaceSpinnerAdapter
import com.abing.letak.utils.MinMaxFilter
import com.abing.letak.viewmodel.ParkingFeeViewModel
import com.abing.letak.viewmodel.UserBookingViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.absoluteValue

class advance_space_selection_fragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private var parkingStart: String? = null
    private val advBookingViewModel: AdvBookingViewModel by activityViewModels()
    private val viewModel: ParkingFeeViewModel by activityViewModels()
    private var durationMinute: Long = 0
    private val args: advance_space_selection_fragmentArgs by navArgs()
    private var spaceType = "Green"
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentAdvanceSpaceSelectionFragmentBinding? = null
    private val binding get() = _binding!!
    private val TAG = "AdvanceSpaceSelectionFragment"
    private var isTimeExist = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdvanceSpaceSelectionFragmentBinding.inflate(layoutInflater)

        initView()
        buttonListener()
        setupSpinner()
        // TODO: check the time (must be between 6-12 hours)
        // TODO: check if user has entered the date and the time
        // TODO: update parking lot name and parking fee

        return binding.root
    }

    private fun setupSpinner() {
        val spaceType = resources.getStringArray(R.array.adv_space_type)
        val adapter = SpaceSpinnerAdapter(requireContext(), spaceType)
        binding.parkingSpaceSpinner.adapter = adapter

        binding.parkingSpaceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    changeColorCardViewColor(p2)
                }

                @SuppressLint("UseCompatLoadingForColorStateLists")
                private fun changeColorCardViewColor(position: Int) {
                    when (position) {
                        0 -> {
                            binding.parkingSpaceCardView.backgroundTintList =
                                resources.getColorStateList(R.color.space_green)
                            this@advance_space_selection_fragment.spaceType = "Green"
                            updateParkingFee()
                        }
                        1 -> {
                            binding.parkingSpaceCardView.backgroundTintList =
                                resources.getColorStateList(R.color.space_yellow)
                            this@advance_space_selection_fragment.spaceType = "Yellow"
                            updateParkingFee()
                        }
                        2 -> {
                            binding.parkingSpaceCardView.backgroundTintList =
                                resources.getColorStateList(R.color.space_red)
                            this@advance_space_selection_fragment.spaceType = "Red"
                            updateParkingFee()
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //nothing here <3
                }

            }
    }

    private fun buttonListener() {
        binding.selectTimeButton.setOnClickListener { timeButton() }
        binding.continueButton.setOnClickListener { continueToAdvConfirmation(it) }
    }

    private fun updateParkingFee() {
        viewModel.calculateFee(
            spaceType,
            binding.durationHour.text.toString(),
            binding.durationMinute.text.toString()
        )
        viewModel.parkingFee.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.parkingFee.text = it.toString()
        })
    }

    private fun continueToAdvConfirmation(view: View) {
        //check if the date and time is according to the business rule
        if (timeInvalid() || !isTimeExist){
            Toast.makeText(requireContext(), R.string.adv_book_rule, Toast.LENGTH_SHORT).show()
            return
        }

        if (redParkingInvalid() && spaceType == "Red"){
            Toast.makeText(requireContext(), R.string.red_valid_below_two_hours, Toast.LENGTH_SHORT).show()
            return
        }

        updateViewModel()

        val action = advance_space_selection_fragmentDirections
            .actionAdvanceSpaceSelectionFragmentToAdvanceConfirmationFragment()
        view.findNavController().navigate(action)
    }

    private fun updateViewModel() {
        advBookingViewModel.setLotId(args.lotId)
        advBookingViewModel.setSpaceType(spaceType)
        advBookingViewModel.setParkingPeriodMinute(getParkingPeriodMinute().toString())
        advBookingViewModel.setParkingStart(parkingStart.toString())
        advBookingViewModel.setParkingEnd(getParkingEnd())
    }

    private fun getParkingEnd(): String{
        val parkingPeriod = getParkingPeriodMinute()
        val hour = Duration.ofHours(parkingPeriod.div(60).toLong())
        val minute = Duration.ofMinutes(parkingPeriod.rem(60).toLong())
        val startTime = parkingStart.toString()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = LocalTime.parse(startTime, formatter)
        val endTime = formattedTime.plus(hour).plus(minute)
        return endTime.format(formatter)
    }

    private fun getParkingPeriodMinute(): Int {
        return (binding.durationHour.text.toString().toInt() * 60) +
                binding.durationMinute.text.toString().toInt()
    }

    private fun redParkingInvalid(): Boolean {
        val totalMinute = (binding.durationHour.text.toString().toInt() * 60) +
                binding.durationMinute.text.toString().toInt()
        Log.d(TAG, "total minute is -> $totalMinute")
        return totalMinute >= 120
    }

    private fun timeInvalid(): Boolean {
        return durationMinute < 360
    }

    private fun timeButton() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(requireContext(), R.style.LETAKDatePickerTheme, this, hour, minute, true)
        tpd.show()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onTimeSet(p0: android.widget.TimePicker?, hour: Int, minute: Int) {
        val time = LocalTime.of(hour, minute)
        val currentTime = LocalTime.now()

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = formatter.format(time)
        val duration = Duration.between(currentTime, time)
        durationMinute = duration.toMinutes().absoluteValue
        Log.d(TAG, "onTimeSet: duration of time is $durationMinute")
        isTimeExist = true
        binding.parkingTime.text = formattedTime
        parkingStart = formattedTime
    }

    private fun initView() {

        //init the parking lot name
        val lotId = args.lotId
        db.collection("parkingLots").document(lotId).get()
            .addOnSuccessListener {
                val parkingLot = it.toObject<ParkingLot>()
                binding.parkingLotName.text = parkingLot?.lotName
            }
        binding.apply {
            parkingTime.text = resources.getString(R.string.set_time)
        }

        //time input init
        binding.apply {
            durationHour.setText("1")
            durationMinute.setText("30")
        }
        binding.durationHour.filters = arrayOf<InputFilter>(MinMaxFilter(0, 23))
        binding.durationMinute.filters = arrayOf<InputFilter>(MinMaxFilter(0, 59))

        updateParkingFee()

    }

}