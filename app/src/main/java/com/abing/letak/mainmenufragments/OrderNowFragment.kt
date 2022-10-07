package com.abing.letak.mainmenufragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.R
import com.abing.letak.data.parkingLotList
import com.abing.letak.databinding.FragmentOrderNowBinding
import com.abing.letak.extendparking.ExtendParkingActivity
import com.abing.letak.model.ParkingLot
import com.abing.letak.ordernowactivity.OrderNowActivity
import com.abing.letak.parkinglotadapter.ParkingLotAdapter

class OrderNowFragment : Fragment() {
    private var _binding: FragmentOrderNowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderNowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val data = parkingLotList
        binding.aLittleInsightRecyclerView.adapter = ParkingLotAdapter(requireContext(), data)
        binding.aLittleInsightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.letakNowButton.setOnClickListener {
            startOrderNowActivity()
        }
    }

    private fun startOrderNowActivity() {
        val intent = Intent(activity, OrderNowActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}