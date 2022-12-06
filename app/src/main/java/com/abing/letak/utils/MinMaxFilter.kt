package com.abing.letak.utils

import android.text.InputFilter
import android.text.Spanned

class MinMaxFilter(): InputFilter {
    private var intMin: Int = 0
    private var intMax: Int = 0

    // Initialized
    constructor(minValue: Int, maxValue: Int) : this() {
        this.intMin = minValue
        this.intMax = maxValue
    }

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(intMin, intMax, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    // Check if input c is in between min a and max b and
    // returns corresponding boolean
    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}