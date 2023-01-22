package com.abing.letak

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.abing.letak.test.Addition
import com.abing.letak.viewmodel.ParkingFeeViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val myClass = Addition()
        val result = myClass.thisAddition(1,2)
        val expected = 3
        assertEquals(expected, result)
    }

}