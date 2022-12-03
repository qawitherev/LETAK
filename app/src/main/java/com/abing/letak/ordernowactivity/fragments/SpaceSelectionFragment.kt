package com.abing.letak.ordernowactivity.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.abing.letak.databinding.FragmentSpaceSelectionBinding
import com.abing.letak.model.ParkingSpace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class SpaceSelectionFragment : Fragment() {
    private var _binding: FragmentSpaceSelectionBinding? = null
    private val binding get() = _binding!!
    private val args: SpaceSelectionFragmentArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()
    private val spaces = mutableListOf<ParkingSpace>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpaceSelectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //getting parking spaces
        getParkingSpaces()

        //button listener
        binding.spaceSelectionContinueButton.setOnClickListener { continueToParkingConfirmation(it) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getParkingSpaces() {
        val spaceRef = db.collection("parkingLots").document(args.lotId).collection("parkingSpaces")
        Log.d("spaceSelection", "${args.lotId}")
        spaceRef.get().addOnSuccessListener {
            for (document in it){
                spaces.add(document.toObject())
            }
            Log.d("spaceSelection", "${spaces.size}")
        }
            .addOnFailureListener {
                Log.d("spaceSelection", "cannot retrieve with ", it)
            }
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