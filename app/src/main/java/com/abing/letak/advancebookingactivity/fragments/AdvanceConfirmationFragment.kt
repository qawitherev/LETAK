package com.abing.letak.advancebookingactivity.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.abing.letak.R
import com.abing.letak.advancebookingactivity.viewmodels.AdvBookingViewModel
import com.abing.letak.databinding.FragmentAdvanceConfirmationBinding
import com.abing.letak.model.AdvBooking
import com.abing.letak.model.ParkingLot
import com.abing.letak.model.ParkingSpace
import com.abing.letak.model.Vehicle
import com.abing.letak.ordernowactivity.adapter.EWalletSpinnerAdapter
import com.abing.letak.ordernowactivity.adapter.VehicleSpinnerAdapter
import com.abing.letak.viewmodel.ParkingFeeViewModel
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdvanceConfirmationFragment : Fragment() {

    private var selectedSpaceId = ""
    private val queriedSpaces = arrayListOf<ParkingSpace>()
    private lateinit var selectedVehicle: Vehicle
    private val vehicles = mutableListOf<Vehicle>()
    private val feeViewModel: ParkingFeeViewModel by activityViewModels()
    private val TAG = "AdvanceConfirmationFragment"
    private val advBookingViewModel: AdvBookingViewModel by activityViewModels()
    private val userIdViewModel: UserIdViewModel by viewModels()
    private lateinit var userRef: DocumentReference
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentAdvanceConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdvanceConfirmationBinding.inflate(layoutInflater)

        initialSetup()
        changeNavigationBarColour()
        parkingDetailSetup()
        setupWalletSpinner()
        setupVehicleSpinner()
        getSpace()

        buttonListener()

        return binding.root
    }

    private fun buttonListener() {
        binding.advanceBookButton.setOnClickListener { advBook() }
    }

    private fun advBook() {
        //this function will insert the data inside firestore
        val advBooking = AdvBooking(
            null,
            advBookingViewModel.lotId.value,
            advBookingViewModel.spaceId.value,
            advBookingViewModel.spaceType.value,
            advBookingViewModel.parkingPeriodMinute.value,
            advBookingViewModel.parkingStart.value,
            advBookingViewModel.parkingEnd.value,
            advBookingViewModel.vecPlate.value,
            advBookingViewModel.eWalletType.value,
            advBookingViewModel.parkingFee.value
        )
        userRef.collection("advBookings").add(advBooking)
            .addOnSuccessListener {
                it.update("advBookingId", it.id)
                advBookingViewModel.setAdvBookingId(it.id)
            }
        userRef.update("advanceBookingStatus", true)
            .addOnSuccessListener {
                userRef.update("activeBookingId", advBookingViewModel.bookingId.value)
            }
        activity?.finish()
    }

    private fun getSpace() {
        val lotId = advBookingViewModel.lotId.value.toString()
        val spaceType = advBookingViewModel.spaceType.value.toString()
        val lotRef = db.collection("parkingLots")
        lotRef.document(lotId).collection("parkingSpaces")
            .whereEqualTo("spaceType", spaceType).whereEqualTo("spaceEmpty", true)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty){
                    Toast.makeText(requireContext(), R.string.no_available_space, Toast.LENGTH_SHORT).show()
                    binding.advanceBookButton.isClickable = false
                }else {
                    binding.advanceBookButton.isClickable = true
                    for (space in it){
                        queriedSpaces.add(space.toObject())
                    }
                    selectedSpaceId = queriedSpaces[0].spaceId.toString()
                    advBookingViewModel.setSpaceId(selectedSpaceId)
                    // TODO: update the selectedSpaceId when user click letak button 
                }
            }
    }

    private fun setupVehicleSpinner() {
        userRef.collection("vehicles").get().addOnSuccessListener {
            vehicles.clear()
            for (document in it) {
                vehicles.add(document.toObject())
            }
            binding.vehicleSpinner.adapter = VehicleSpinnerAdapter(requireContext(), vehicles)
            binding.vehicleSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        selectedVehicle = vehicles[position]
                        advBookingViewModel.setVecPlate(selectedVehicle.vecPlate.toString())
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
//                        nothing happens here (●'◡'●)
                    }
                }
        }
    }

    private fun setupWalletSpinner() {
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
                            advBookingViewModel.setEWalletType("Boost")
                        }
                        1 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.touch_n__go_logo))
                            advBookingViewModel.setEWalletType("Touch n' Go")
                        }
                        2 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.mae_logo))
                            advBookingViewModel.setEWalletType("MAE")
                        }
                    }
                }
            }
    }

    private fun parkingDetailSetup() {
        val lotId = advBookingViewModel.lotId.value.toString()
        db.collection("parkingLots").document(lotId).get().addOnSuccessListener {
            val lot = it.toObject<ParkingLot>()!!
            binding.parkingLotName.text = lot.lotName
            val parkingStart = advBookingViewModel.parkingStart.value.toString()
            val parkingEnd = advBookingViewModel.parkingEnd.value.toString()
            binding.parkingDuration.text = "$parkingStart - $parkingEnd"
            binding.parkingFee.text = parkingFeeFormatter()
            binding.spaceType.text = advBookingViewModel.spaceType.value
        }
    }

    private fun parkingFeeFormatter(): String {
        val parkingFee = feeViewModel.parkingFee.value
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return currencyFormatter.format(parkingFee)
    }

    private fun changeNavigationBarColour() {
        val activity = requireActivity()
        val window = activity.window
        val context = activity.applicationContext
        window.setNavigationBarColor(ContextCompat.getColor(context, R.color.white))
    }

    private fun initialSetup() {
        db = FirebaseFirestore.getInstance()
        userRef = db.collection("users").document(userIdViewModel.userId)
    }
}