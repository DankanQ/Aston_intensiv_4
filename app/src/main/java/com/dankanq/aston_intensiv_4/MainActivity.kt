package com.dankanq.aston_intensiv_4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var clockView: ClockView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clockView = findViewById(R.id.clock_view)

        with(clockView) {
            setHourHandColor(R.color.black)
            setMinuteHandColor(R.color.purple_500)
            setSecondHandColor(R.color.purple_700)

            setHourHandSize(6)
            setMinuteHandSize(4)
            setSecondHandSize(2)
        }
    }
}