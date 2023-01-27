package com.abing.letak.monthlypassactivity.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.abing.letak.R
import com.abing.letak.databinding.FragmentMonthlyPassParkingLotSelectionBinding
import com.abing.letak.databinding.FragmentMonthlyPassSpaceSelectionBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.monthlypassactivity.viewmodel.PassBookingViewModel
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*

class MonthlyPassSpaceSelection : Fragment(), DatePickerDialog.OnDateSetListener {

    private var dateExist = false
    private val passBookingViewModel: PassBookingViewModel by activityViewModels()
    private val TAG = "MonthPassSpaceSelection"
    private val args: MonthlyPassSpaceSelectionArgs by navArgs()
    private val userIdViewModel: UserIdViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentMonthlyPassSpaceSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthlyPassSpaceSelectionBinding.inflate(layoutInflater)
        initSetup()
        initInfo()
        return binding.root
    }

    private fun initSetup() {
        db = FirebaseFirestore.getInstance()
    }

    private fun initInfo() {
        db.collection("parkingLots").document(args.lotId).get().addOnSuccessListener {
            val parkingLot = it.toObject<ParkingLot>()!!
            binding.parkingLotName.text = parkingLot.lotName
            passBookingViewModel.setLotName(parkingLot.lotName!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.parkingDateCard.setOnClickListener {
            pickStartDate()
        }
        binding.spaceSelectionContinueButton.setOnClickListener { continueToPassConfirmation(it) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun pickStartDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), R.style.LETAKDatePickerTheme,
        this, year, month, day)
        dpd.show()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val c = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        val startDate = c.time
        val today = Calendar.getInstance()
        val todayDay = today.time
        if (startDate.after(todayDay)){
            val formatter = SimpleDateFormat("dd-MMM-yyyy")
            val formattedStartDate = formatter.format(startDate)
            passBookingViewModel.setStartDate(formattedStartDate)

            //adding 30 days into the days
            c.add(Calendar.DAY_OF_MONTH, 30)
            val endDate = c.time
            val formattedEndDate = formatter.format(endDate)
            passBookingViewModel.setEndDate(formattedEndDate)
            binding.apply {
                passStartDate.text = passBookingViewModel.startDate.value
                passEndDate.text = passBookingViewModel.endDate.value
            }
            dateExist = true
        }else {
            Toast.makeText(requireContext(), R.string.day_after_today, Toast.LENGTH_SHORT).show()
            return
        }

    }

    private fun continueToPassConfirmation(view: View?) {
        if (!dateExist){
            Toast.makeText(requireContext(), R.string.pick_a_date, Toast.LENGTH_SHORT).show()
            return
        }
        passBookingViewModel.setLotId(args.lotId)
        val action = MonthlyPassSpaceSelectionDirections
            .actionMonthlyPassSpaceSelectionToMonthlyPassConfirmationFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}