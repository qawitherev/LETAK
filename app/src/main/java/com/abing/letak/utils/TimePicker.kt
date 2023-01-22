package com.abing.letak.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.abing.letak.R
import java.util.*

class TimePicker : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val TAG = "TimePicker"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity, R.style.LETAKDatePickerTheme, this, hour, minute,
            true
        )
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d(TAG, "onTimeSet: $hourOfDay:$minute")
    }
}