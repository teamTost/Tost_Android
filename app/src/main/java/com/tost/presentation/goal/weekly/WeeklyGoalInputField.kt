package com.tost.presentation.goal.weekly

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
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

    val countText: String
        get() = binding.textCount.text.toString()

    init {
        binding.root.setOnClickListener { deploySoftKeyboard() }
    }

    private fun deploySoftKeyboard() {
        binding.textCount.requestFocus()
        val imm = getSystemService(context, InputMethodManager::class.java)
        imm?.showSoftInput(binding.textCount, InputMethodManager.SHOW_IMPLICIT)
    }

    fun addTextChangedListener(listener: (text: Editable?) -> Unit) {
        binding.textCount.addTextChangedListener {
            val textColor = if (it.isNullOrBlank()) R.color.blue_grey_cf else R.color.blue_grey_22
            binding.textProblem.setTextColor(ContextCompat.getColor(context, textColor))
            listener(it)
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("countText")
        fun bindingCountText(view: WeeklyGoalInputField, text: String?) {
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "app:countText", event = "countTextAttrChanged")
        fun getCountText(view: WeeklyGoalInputField): String {
            return view.countText
        }

        @JvmStatic
        @BindingAdapter("countTextAttrChanged")
        fun setCountTextInverseListener(
            view: WeeklyGoalInputField,
            listener: InverseBindingListener
        ) {
            view.addTextChangedListener { listener.onChange() }
        }
    }
}
