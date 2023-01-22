package com.abing.letak.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.abing.letak.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DatePicker: DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val TAG = "DatePicker"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), R.style.LETAKDatePickerTheme, this, year, month, day)
    }
    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        Log.d(TAG, "onDateSet: selected date is -> $day/${month+1}/$year")
    }
}