package com.abing.letak.mainmenufragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.R
import com.abing.letak.databinding.FragmentOrderNowBinding
import com.abing.letak.extendparking.ExtendParkingActivity
import com.abing.letak.model.ParkingLot
import com.abing.letak.ordernowactivity.OrderNowActivity
import com.abing.letak.parkinglotadapter.ParkingLotAdapter
import com.abing.letak.viewmodel.UserBookingViewModel
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class OrderNowFragment : Fragment() {
    private var parkingDurationMilis: Long = 0
    private var parkingStartMilis: Long = 0
    private lateinit var bookingRef: DocumentReference
    private var activeBookingId: String = ""
    private var _binding: FragmentOrderNowBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var userRef: DocumentReference
    private val parkingLots: MutableList<ParkingLot> = mutableListOf()
    private val userIdViewModel: UserIdViewModel by activityViewModels()
    private val userBookingViewModel: UserBookingViewModel by activityViewModels()
    private var orderNowStatus: Boolean? = false
    private var advanceBookingStatus: Boolean? = false
    private val TAG = "OrderNowFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderNowBinding.inflate(inflater, container, false)

        initialSetup()
        checkUserStatus()
        binding.unparkButton.setOnClickListener { unparkStart() }
        return binding.root
    }

    private fun unparkStart() {
        var lotId = ""
        var spaceId = ""
        userRef.update("orderNowStatus", false)
        userRef.update("activeBookingId", null)
        bookingRef.get().addOnSuccessListener {
            lotId = it.getString("lotId").toString()
            spaceId = it.getString("spaceId").toString()
            val spaceRef = db.collection("parkingLots").document(lotId).collection("parkingSpaces").document(spaceId)
            spaceRef.update("spaceEmpty", true)
        }
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        val newFragment = OrderNowFragment()
        fragmentTransaction?.replace(R.id.fragment_container, newFragment)
        fragmentTransaction?.commit()
    }

    private fun checkUserStatus() {
        //function to check if user has ordernow or advance booking
        //user can either have order now or advance booking at a time
        //monthly pass is independent of these 2
        userRef.get().addOnSuccessListener {
            orderNowStatus = it.getBoolean("orderNowStatus")
            advanceBookingStatus = it.getBoolean("advanceBookingStatus")
            if (orderNowStatus == false && advanceBookingStatus == false){
                binding.apply {
                    noActiveGroup.visibility = View.VISIBLE
                    orderNowActiveGroup.visibility = View.GONE
                    noActiveParking()
                }
            }
            if (orderNowStatus == true && advanceBookingStatus == false){
                binding.apply {
                    orderNowActiveCard.visibility = View.VISIBLE
                    noActiveGroup.visibility = View.GONE
                    orderNowActive()
                }
            }
        }
    }

    private fun orderNowActive() {
        userRef.get()
            .addOnSuccessListener {
                activeBookingId = it.getString("activeBookingId").toString()
                bookingRef = userRef.collection("bookings").document(activeBookingId)
                setupActiveBookingRef()
            }
            .addOnFailureListener {
                Log.d(TAG, "orderNowActive: User not found")
            }
    }

    private fun setupActiveBookingRef() {
        bookingRef.get().addOnSuccessListener {
            val lotId = it.getString("lotId").toString()
            val spaceType = it.getString("spaceType")
            parkingStartMilis = it.getLong("parkingStartMilis")!!
            val parkingPeriodMinute = it.getLong("parkingPeriodMinute")!!
            parkingDurationMilis = parkingPeriodMinute * 60 * 1000
            Log.d(TAG, "setupActiveBookingRef: parkingPeriodMinute->$parkingDurationMilis")
            displayTimeRemaining(parkingStartMilis, parkingDurationMilis)
            setupSpaceTypeCard(spaceType)

            //get lot name using lotId
            db.collection("parkingLots").document(lotId).get().addOnSuccessListener {
                val parkingLotName = it.getString("lotName")
                binding.parkingLotName.text = parkingLotName
            }

            binding.extendButton.setOnClickListener { extendParking() }

        }
    }

    private fun displayTimeRemaining(parkingStartMilis: Long, parkingDurationMilis: Long) {
        val currentTimeMilis = System.currentTimeMillis()
        val endTimeMilis = parkingStartMilis.plus(parkingDurationMilis)
        val remainingMilis = endTimeMilis.minus(currentTimeMilis)
        //convert milis to minutes
        //1minute = 1000 * 60
        val remainingMinute = remainingMilis.div(60*1000)
        binding.elapsedTime.text = remainingMinute.toString()
        if (remainingMinute < 10){
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.parking_expiring)
                .setMessage(R.string.parking_expiring_soon)
                .setCancelable(true)
                .setPositiveButton(R.string.extend){dialogInterface, it ->
                    extendParking()
                }
                .setNegativeButton(R.string.unpark){dialogInterface, it ->
                    dialogInterface.cancel()
                }
                .show()
            if (remainingMinute < 1){
                binding.elapsedTime.apply {
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    text = getString(R.string.parking_expired)
                    unparkStart()
                }
            }
        }
    }

    private fun extendParking() {
        val intent = Intent(requireContext(), ExtendParkingActivity::class.java)
        intent.putExtra("activeBookingId", activeBookingId)
        startActivity(intent)
    }


    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun setupSpaceTypeCard(spaceType: String?) {
        if (spaceType == "Green"){
            binding.spaceTypeCard.backgroundTintList = resources.getColorStateList(R.color.space_green)
        }else if (spaceType == "Yellow"){
            binding.spaceTypeCard.backgroundTintList = resources.getColorStateList(R.color.space_yellow)
        }else{
            binding.spaceTypeCard.backgroundTintList = resources.getColorStateList(R.color.space_red)
        }
    }

    private fun noActiveParking() {
        getParkingLot()
        binding.letakNowButton.setOnClickListener {
            startOrderNowActivity()
        }
    }

    private fun initialSetup() {
        userRef = db.collection("users").document(userIdViewModel.userId)
    }

    private fun getParkingLot() {
        val lotRef = db.collection("parkingLots")
        lotRef.get()
            .addOnSuccessListener {
                for (document in it) {
                    parkingLots.add(document.toObject())
                    Log.d("order parking lot list", "${parkingLots.size}")
                }
                initRecyclerView()
            }
            .addOnFailureListener {
                Log.w("getParkingLot", "getParkingLot failed with", it)
            }
    }

    private fun initRecyclerView() {
        binding.aLittleInsightRecyclerView.adapter =
            ParkingLotAdapter(requireContext(), parkingLots)
        binding.aLittleInsightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun startOrderNowActivity() {
        val intent = Intent(activity, OrderNowActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parkingLots.clear()
        _binding = null
    }
}