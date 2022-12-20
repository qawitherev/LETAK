package com.abing.letak.ordernowactivity.fragments

import android.annotation.SuppressLint
import android.icu.number.NumberFormatter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.abing.letak.R
import com.abing.letak.databinding.FragmentParkingConfirmationBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.model.UserBooking
import com.abing.letak.model.Vehicle
import com.abing.letak.ordernowactivity.adapter.EWalletSpinnerAdapter
import com.abing.letak.ordernowactivity.adapter.VehicleSpinnerAdapter
import com.abing.letak.viewmodel.ParkingFeeViewModel
import com.abing.letak.viewmodel.UserBookingViewModel
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.NumberFormat
import java.util.Locale

class ParkingConfirmationFragment : Fragment() {
    private var _binding: FragmentParkingConfirmationBinding? = null
    private val binding get() = _binding!!
    private var selectedEWallet = ""
    private lateinit var db: FirebaseFirestore
    private lateinit var userRef: DocumentReference
    private val userIdViewModel: UserIdViewModel by viewModels()
    private val vehicles: ArrayList<Vehicle> = arrayListOf()
    private val userBookingViewModel: UserBookingViewModel by activityViewModels()
    private val parkingFeeViewModel: ParkingFeeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParkingConfirmationBinding.inflate(layoutInflater)

        Log.d("ParkingConfirmationFragment", "the userBookingViewModel: ${userBookingViewModel.parkingPeriodMinute.value}")
        initialSetup()

        parkingDetailsSetup()
        setupEWalletSpinner()
        setupVehicleSpinner()

        return binding.root
    }

    private fun parkingDetailsSetup() {
        val lotId = userBookingViewModel.lotId.value
        db.collection("parkingLots").document(lotId.toString()).get().addOnSuccessListener {
            val lot = it.toObject<ParkingLot>()
            binding.parkingLotName.text = lot?.lotName
            binding.parkingDuration.text = changeToHHMM()
            binding.parkingFee.text = parkingFee()
            binding.spaceType.text = userBookingViewModel.spaceType.value
        }

    }

    private fun parkingFee(): String {
        val parkingFee = parkingFeeViewModel.parkingFee.value
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val formattedAmount = currencyFormatter.format(parkingFee)
        return formattedAmount.toString()
    }

    private fun changeToHHMM(): String{
        val parkingPeriodMinute = userBookingViewModel.parkingPeriodMinute.value
        val hour = parkingPeriodMinute?.div(60)
        val minute = parkingPeriodMinute?.mod(60)
        return "$hour hour $minute minutes"
    }

    private fun initialSetup() {
        db = FirebaseFirestore.getInstance()
        userRef = db.collection("users").document(userIdViewModel.userId)
    }

    private fun setupVehicleSpinner() {
        getVehicles()
        binding.vehicleSpinner.adapter = VehicleSpinnerAdapter(requireContext(), vehicles)
    }

    private fun getVehicles() {
        userRef.collection("vehicles").get()
            .addOnSuccessListener {
                for (vehicle in it){
                    vehicles.add(vehicle.toObject())
                }
            }

    }

    private fun setupEWalletSpinner() {
        val eWallet = arrayOf("Boost", "Touch n' Go", "MAE")
        binding.ewalletSpinner.adapter = EWalletSpinnerAdapter(requireContext(), eWallet)

        binding.ewalletSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    changeEWallet(p2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //nothing happen
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                private fun changeEWallet(position: Int) {
                    when (position) {
                        0 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.boost_logo))
                            selectedEWallet = "Boost"
                        }
                        1 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.touch_n__go_logo))
                            selectedEWallet = "Touch n' Go"
                        }
                        2 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.mae_logo))
                            selectedEWallet = "MAE"
                        }
                    }
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.letakButton.setOnClickListener { continueToParkingConfirmed(it) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun continueToParkingConfirmed(view: View) {
        vehicles.clear()
        val action = ParkingConfirmationFragmentDirections
            .actionParkingConfirmationFragmentToBookingConfirmedFragment()
        view.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}