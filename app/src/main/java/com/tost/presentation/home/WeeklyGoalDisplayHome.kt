package com.tost.presentation.home

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.tost.R
import com.tost.databinding.WidgetWeeklyGoalHomeBinding

/**
 * Created By Malibin
 * on 12월 25, 2020
 */

class WeeklyGoalDisplayHome @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = WidgetWeeklyGoalHomeBinding.inflate(LayoutInflater.from(context), this)

    var count: Int = 0
        set(value) {
            setCountText(value)
            field = value
        }

    var part: Int = 0
        set(value) {
            setPartText(value)
            field = value
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.WeeklyGoalDisplayHome) {
            applyAttributes(this)
        }
    }

    private fun applyAttributes(typedArray: TypedArray) {
        count = typedArray.getInt(R.styleable.WeeklyGoalDisplayHome_count, 0)
        part = typedArray.getInt(R.styleable.WeeklyGoalDisplayHome_part_number, -1)
    }

    private fun setCountText(count: Int) {
        binding.textCount.text = count.toString()
    }

    private fun setPartText(part: Int) {
        binding.textPart.text = context.getString(R.string.part__n, part)
    }
}
