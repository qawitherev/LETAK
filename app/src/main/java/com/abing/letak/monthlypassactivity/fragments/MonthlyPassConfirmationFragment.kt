package com.abing.letak.monthlypassactivity.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import com.abing.letak.R
import com.abing.letak.databinding.FragmentMonthlyPassConfirmationBinding
import com.abing.letak.model.PassBooking
import com.abing.letak.monthlypassactivity.viewmodel.PassBookingViewModel
import com.abing.letak.ordernowactivity.adapter.EWalletSpinnerAdapter
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.FirebaseFirestoreKtxRegistrar

class MonthlyPassConfirmationFragment : Fragment() {

    private val userIdViewModel: UserIdViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private val eWallets = arrayOf("Boost", "Touch n' Go", "MAE")
    private val passBookingViewModel: PassBookingViewModel by activityViewModels()
    private var _binding: FragmentMonthlyPassConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthlyPassConfirmationBinding.inflate(layoutInflater)

        initSetup()
        initPassDetails()
        initEWalletSpinner()
        binding.purchaseButton.setOnClickListener { purchasePass() }

        return binding.root
    }

    private fun initSetup() {
        db = FirebaseFirestore.getInstance()
    }

    private fun purchasePass() {
        updateFirestore()
    }

    private fun updateFirestore() {

        val userRef = db.collection("users").document(userIdViewModel.userId)
        userRef.update("monthlyPassStatus", true)

        val pass = PassBooking(
            null,
            passBookingViewModel.lotId.value,
            passBookingViewModel.lotName.value,
            passBookingViewModel.startDate.value,
            passBookingViewModel.endDate.value,
            passBookingViewModel.eWalletType.value,
        )
        db.collection("users").document(userIdViewModel.userId).collection("pass")
            .add(pass).addOnSuccessListener {
                it.update("passId", it.id)
            }
    }

    private fun initEWalletSpinner() {
        binding.ewalletSpinner.adapter = EWalletSpinnerAdapter(requireContext(), eWallets)
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
                            passBookingViewModel.setEwalletType("Boost")
                        }
                        1 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.touch_n__go_logo))
                            passBookingViewModel.setEwalletType("Touch n' Go")
                        }
                        2 -> {
                            binding.ewalletLogo.setImageDrawable(resources.getDrawable(R.drawable.mae_logo))
                            passBookingViewModel.setEwalletType("MAE")
                        }
                    }
                }
            }
    }

    private fun initPassDetails() {
        //parking lot name, start date and end date
        binding.parkingLotName.text = passBookingViewModel.lotName.value
        val startEndDate = "${passBookingViewModel.startDate.value} to ${passBookingViewModel.endDate.value}"
        binding.passDurationDate.text = startEndDate
    }
}