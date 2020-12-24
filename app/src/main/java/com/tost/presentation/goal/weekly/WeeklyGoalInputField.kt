package com.tost.presentation.goal.weekly

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.withStyledAttributes
import com.tost.R
import com.tost.databinding.WidgetWeeklyGoalInputBinding


/**
 * Created By Malibin
 * on 12월 24, 2020
 */

class WeeklyGoalInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = WidgetWeeklyGoalInputBinding.inflate(LayoutInflater.from(context), this)

    var countText: String
        get() = binding.textCount.text.toString()
        set(value) {
            setCount(value)
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.AudioStateButton) { applyAttributes(this) }
        binding.root.setOnClickListener { deploySoftKeyboard() }
    }

    private fun applyAttributes(typedArray: TypedArray) {
        val text = typedArray.getString(R.styleable.AudioStateButton_state)
        countText = text.orEmpty()
    }

    private fun deploySoftKeyboard() {
        binding.textCount.requestFocus()
        val imm = getSystemService(context, InputMethodManager::class.java)
        imm?.showSoftInput(binding.textCount, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setCount(input: String) {
        binding.textCount.setText(input)
        val textColor = if (input.isBlank()) R.color.blue_grey_cf else R.color.blue_grey_22
        binding.textProblem.setTextColor(ContextCompat.getColor(context, textColor))
    }
}
