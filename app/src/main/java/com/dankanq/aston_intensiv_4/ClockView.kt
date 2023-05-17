package com.dankanq.aston_intensiv_4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    private val hours = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val radius by lazy {
        (min(height, width) / 2 - margin).toInt()
    }
    private var margin = dipToPx(8)
    private var textSize = dipToPx(18)

    private var hourHandColor = ContextCompat.getColor(context, R.color.defaultHourHandColor)
    private var minuteHandColor = ContextCompat.getColor(context, R.color.defaultMinuteHandColor)
    private var secondHandColor = ContextCompat.getColor(context, R.color.defaultSecondHandColor)
    private var hourHandSize = resources.getDimension(R.dimen.defaultHourHandSize)
    private var minuteHandSize = resources.getDimension(R.dimen.defaultMinuteHandSize)
    private var secondHandSize = resources.getDimension(R.dimen.defaultSecondHandSize)

    private val rect = Rect()

    init {
        setAttributes(attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.scale(0.8f, 0.8f, (width / 2).toFloat(), (height / 2).toFloat())

        drawClockBackground(canvas)
        drawNumbers(canvas)
        drawHands(canvas)
    }

    private fun setAttributes(attributes: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attributes, R.styleable.ClockView, 0, 0)

        hourHandColor = ContextCompat.getColor(
            context,
            typedArray.getResourceId(
                R.styleable.ClockView_hourHandColor,
                R.color.defaultHourHandColor
            )
        )
        minuteHandColor = ContextCompat.getColor(
            context,
            typedArray.getResourceId(
                R.styleable.ClockView_minuteHandColor,
                R.color.defaultMinuteHandColor
            )
        )
        secondHandColor = ContextCompat.getColor(
            context,
            typedArray.getResourceId(
                R.styleable.ClockView_secondHandColor,
                R.color.defaultSecondHandColor
            )
        )
        hourHandSize = typedArray.getDimension(
            R.styleable.ClockView_hourHandSize,
            resources.getDimension(R.dimen.defaultHourHandSize)
        )
        minuteHandSize = typedArray.getDimension(
            R.styleable.ClockView_minuteHandSize,
            resources.getDimension(R.dimen.defaultMinuteHandSize)
        )
        secondHandSize = typedArray.getDimension(
            R.styleable.ClockView_secondHandSize,
            resources.getDimension(R.dimen.defaultSecondHandSize)
        )

        typedArray.recycle()
    }

    private fun drawClockBackground(canvas: Canvas) {
        paint.color = ContextCompat.getColor(context, R.color.defaultBackgroundColor)
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (radius + 50).toFloat(),
            paint
        )

        paint.reset()
    }

    private fun drawNumbers(canvas: Canvas) {
        paint.textSize = textSize

        for (hour in hours) {
            val hourAsString = hour.toString()

            paint.getTextBounds(hourAsString, 0, hourAsString.length, rect)
            val angle = Math.PI / 6 * (hour - 3)
            val x = (width / 2 + cos(angle) * radius - rect.width() / 2).toFloat()
            val y = ((height / 2).toDouble() + sin(angle) * radius + (rect.height() / 2)).toFloat()

            canvas.drawText(hourAsString, x, y, paint)
        }
    }

    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"))
        val hour = calendar.get(Calendar.HOUR)

        drawHandLine(canvas, (hour + calendar.get(Calendar.MINUTE) / 60f) * 5f, HandType.HOUR)
        drawHandLine(canvas, calendar.get(Calendar.MINUTE).toFloat(), HandType.MINUTE)
        drawHandLine(canvas, calendar.get(Calendar.SECOND).toFloat(), HandType.SECOND)

        postInvalidateDelayed(1000L)

        paint.reset()
    }

    private fun drawHandLine(canvas: Canvas, value: Float, handType: HandType) {
        val angle = Math.PI * value / 30 - Math.PI / 2

        val handRadius = when (handType) {
            HandType.HOUR -> radius - radius / 2
            HandType.MINUTE -> radius - radius / 6
            HandType.SECOND -> radius - radius / 10
        }
        paint.color = when (handType) {
            HandType.HOUR -> hourHandColor
            HandType.MINUTE -> minuteHandColor
            HandType.SECOND -> secondHandColor
        }
        paint.strokeWidth = when (handType) {
            HandType.HOUR -> hourHandSize
            HandType.MINUTE -> minuteHandSize
            HandType.SECOND -> secondHandSize
        }
        paint.strokeCap = Paint.Cap.ROUND

        canvas.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
    }

    fun setHourHandColor(value: Int) {
        hourHandColor = ContextCompat.getColor(context, value)
    }

    fun setMinuteHandColor(value: Int) {
        minuteHandColor = ContextCompat.getColor(context, value)
    }

    fun setSecondHandColor(value: Int) {
        secondHandColor = ContextCompat.getColor(context, value)
    }

    fun setHourHandSize(value: Int) {
        hourHandSize = dipToPx(value)
    }

    fun setMinuteHandSize(value: Int) {
        minuteHandSize = dipToPx(value)
    }

    fun setSecondHandSize(value: Int) {
        secondHandSize = dipToPx(value)
    }
}