package com.abing.letak.ordernowactivity.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import java.util.*

class ParkingConfirmationFragment : Fragment() {
    private var _binding: FragmentParkingConfirmationBinding? = null
    private val binding get() = _binding!!
    private var selectedEWallet = ""
    private lateinit var db: FirebaseFirestore
    private lateinit var userRef: DocumentReference
    private val userIdViewModel: UserIdViewModel by viewModels()
    private val vehiclePlates = mutableListOf<String>()
    private val userBookingViewModel: UserBookingViewModel by activityViewModels()
    private val parkingFeeViewModel: ParkingFeeViewModel by activityViewModels()
    private var vehicles = mutableListOf<Vehicle>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParkingConfirmationBinding.inflate(layoutInflater)

        initialSetup()

        changeNavigationBarColour()
        parkingDetailsSetup()
        setupEWalletSpinner()
        setupVehicleSpinner()

        return binding.root
    }

    private fun setupVehicleSpinner() {
        userRef.collection("vehicles").get()
            .addOnSuccessListener {
                vehicles.clear()
                for (document in it){
                    vehicles.add(document.toObject())
                }
                Log.d("ParkingConfirmationFragment", "vehicles->$vehicles")
                binding.vehicleSpinner.adapter = VehicleSpinnerAdapter(requireContext(), vehicles)
            }
    }


    private fun changeNavigationBarColour() {
        val activity = requireActivity()
        val window = activity.window
        val context = activity.applicationContext
        window.setNavigationBarColor(ContextCompat.getColor(context, R.color.white))
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

    private fun changeToHHMM(): String {
        val parkingPeriodMinute = userBookingViewModel.parkingPeriodMinute.value
        val hour = parkingPeriodMinute?.div(60)
        val minute = parkingPeriodMinute?.mod(60)
        return "$hour hour $minute minutes"
    }

    private fun initialSetup() {
        db = FirebaseFirestore.getInstance()
        userRef = db.collection("users").document(userIdViewModel.userId)
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
                            userBookingViewModel.setEWalletType("Boost")
                        }
                        1 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.touch_n__go_logo))
                            selectedEWallet = "Touch n' Go"
                            userBookingViewModel.setEWalletType("Touch n' Go")
                        }
                        2 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.mae_logo))
                            selectedEWallet = "MAE"
                            userBookingViewModel.setEWalletType("MAE")
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
        vehiclePlates.clear()
        insertBookingFirestore()
        val action = ParkingConfirmationFragmentDirections
            .actionParkingConfirmationFragmentToBookingConfirmedFragment()
        view.findNavController().navigate(action)
    }

    private fun insertBookingFirestore() {
        //create userBooking object from userBookingViewModel
        val userBooking = UserBooking()
        userBooking.lotId = userBookingViewModel.lotId.value
        userBooking.parkingPeriodMinute = userBookingViewModel.parkingPeriodMinute.value
        userBooking.spaceType = userBookingViewModel.spaceType.value
        userBooking.eWalletType = userBookingViewModel.eWalletType.value
        userRef.collection("bookings").add(userBooking).addOnSuccessListener {
            it.update("bookingId", it.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}