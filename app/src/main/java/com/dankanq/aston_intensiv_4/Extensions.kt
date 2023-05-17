package com.dankanq.aston_intensiv_4

import android.view.View

fun View.dipToPx(value: Int): Float = resources.displayMetrics.density * value
