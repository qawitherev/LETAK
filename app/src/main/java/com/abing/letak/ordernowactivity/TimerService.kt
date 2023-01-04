package com.abing.letak.ordernowactivity

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class TimerService: Service() {

    private var timer: CountDownTimer? = null
    private val TAG = "TimerService"

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val duration: Long = intent!!.getLongExtra("duration", 0)

        timer = object: CountDownTimer(duration, 1000){
            override fun onTick(p0: Long) {
                Log.d(TAG, "onTick: ${p0 / 1000} has elapsed")
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish: timer is finished")
            }
        }.start()

        return START_STICKY
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }

}