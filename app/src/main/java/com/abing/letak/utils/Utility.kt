package com.abing.letak.utils

import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

fun lightStatusBar(window: Window, isLight: Boolean, isLightNavBar: Boolean) {
    val wic = WindowInsetsControllerCompat(window, window.decorView)
    wic.isAppearanceLightStatusBars = isLight
    wic.isAppearanceLightNavigationBars = isLightNavBar
}

fun setFullScreen(window: Window){
    WindowCompat.setDecorFitsSystemWindows(window, false)
}