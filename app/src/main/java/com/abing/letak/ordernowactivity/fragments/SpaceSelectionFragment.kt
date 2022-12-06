package com.abing.letak.ordernowactivity.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.abing.letak.R
import com.abing.letak.databinding.FragmentSpaceSelectionBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.model.ParkingSpace
import com.abing.letak.ordernowactivity.adapter.SpaceSpinnerAdapter
import com.abing.letak.utils.MinMaxFilter
import com.abing.letak.viewmodel.ParkingFeeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.Observer

class SpaceSelectionFragment : Fragment() {
    private var _binding: FragmentSpaceSelectionBinding? = null
    private val binding get() = _binding!!
    private val args: SpaceSelectionFragmentArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()
    private val spaces = mutableListOf<ParkingSpace>()
    private val viewModel: ParkingFeeViewModel by viewModels()
    private var spaceType = "Green"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpaceSelectionBinding.inflate(layoutInflater)

        initView()
        setupSpinner()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //getting parking spaces
        getParkingSpaces()

        //button listener
        binding.spaceSelectionContinueButton.setOnClickListener { continueToParkingConfirmation(it) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupSpinner() {
        val spaceType = resources.getStringArray(R.array.space_type)
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
                            this@SpaceSelectionFragment.spaceType = "Green"
                        }
                        1 -> {
                            binding.parkingSpaceCardView.backgroundTintList =
                                resources.getColorStateList(R.color.space_yellow)
                            this@SpaceSelectionFragment.spaceType = "Yellow"
                        }
                        2 -> {
                            binding.parkingSpaceCardView.backgroundTintList =
                                resources.getColorStateList(R.color.space_red)
                            this@SpaceSelectionFragment.spaceType = "Red"
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //nothing here <3
                }

            }
    }

    private fun initView() {
        binding.durationHour.setText("1")
        binding.durationMinute.setText("30")

        updateParkingFee()
        focusChangeListener()

        //input filer such that  <= hour <= 23
        //1 <= minute <= 59
        binding.durationHour.filters = arrayOf<InputFilter>(MinMaxFilter(0, 23))
        binding.durationMinute.filters = arrayOf<InputFilter>(MinMaxFilter(0, 59))

    }


    private fun focusChangeListener() {
        binding.apply {
            durationHour.setOnFocusChangeListener { view, focus ->
                if (!focus){
                    updateParkingFee()
                    Log.d("spaceSelectionFragment", "hour lost focus")
                }else {
                    Toast.makeText(requireContext(), R.string.enter_hour, Toast.LENGTH_SHORT).show()
                }
            }
            durationMinute.setOnFocusChangeListener { view, focus ->
                if (!focus){
                    updateParkingFee()
                    Log.d("spaceSelectionFragment", "minute lost focus")
                }else {
                    Toast.makeText(requireContext(), R.string.enter_minute, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateParkingFee() {
//        Log.d("spaceSelectionFragment", "spaceType: $spaceType")
        viewModel.calculateFee(
            spaceType,
            binding.durationHour.text.toString(),
            binding.durationMinute.text.toString()
        )
        Log.d("spaceSelectionFragment", "parking fee is ${viewModel.parkingFee.value}")
        viewModel.parkingFee.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.parkingFee.text = it.toString()
        })
    }


    private fun getParkingSpaces() {
        val spaceRef = db.collection("parkingLots").document(args.lotId).collection("parkingSpaces")
        spaceRef.get().addOnSuccessListener {
            for (document in it) {
                spaces.add(document.toObject())
            }
            Log.d("spaceSelection", "${spaces.size}")
        }
            .addOnFailureListener {
                Log.d("spaceSelection", "cannot retrieve with ", it)
            }
        return
    }

    private fun continueToParkingConfirmation(view: View) {
        val action = SpaceSelectionFragmentDirections
            .actionSpaceSelectionFragmentToParkingConfirmationFragment()
        view.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        spaces.clear()
        _binding = null
    }

}