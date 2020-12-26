package com.tost.presentation.goal.weekly.change

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.tost.databinding.WidgetWeeklyGoalChangeBinding

/**
 * Created By Malibin
 * on 12월 26, 2020
 */

class WeeklyGoalChangeField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = WidgetWeeklyGoalChangeBinding.inflate(LayoutInflater.from(context), this)

    var existCount: String = ""
        set(value) {
            field = value
            binding.textCountExist.text = value
        }

    var countToChange: String = ""
        get() = binding.textCountToChange.text.toString()
        set(value) {
            field = value
            setCountToChangeText(value)
        }

    init {
        binding.root.setOnClickListener { deploySoftKeyboard() }
    }

    private fun deploySoftKeyboard() {
        binding.textCountToChange.requestFocus()
        if (countToChange.isNotEmpty()) {
            binding.textCountToChange.setSelection(countToChange.length)
        }
        val imm = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        imm?.showSoftInput(binding.textCountToChange, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setCountToChangeText(text: String) {
        binding.textCountToChange.setText(text)
    }

    fun addTextChangedListener(listener: (text: Editable?) -> Unit) {
        binding.textCountToChange.addTextChangedListener { listener(it) }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("existCount")
        fun bindingExistCount(view: WeeklyGoalChangeField, text: Int?) {
            view.existCount = text.toString()
        }

        @JvmStatic
        @BindingAdapter("count")
        fun bindingCountText(view: WeeklyGoalChangeField, text: String?) {
            if (view.countToChange != text) {
                view.countToChange = text.orEmpty()
            }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "count", event = "countAttrChanged")
        fun getCountText(view: WeeklyGoalChangeField): String {
            return view.countToChange
        }

        @JvmStatic
        @BindingAdapter("countAttrChanged")
        fun setCountTextInverseListener(
            view: WeeklyGoalChangeField,
            listener: InverseBindingListener
        ) {
            view.addTextChangedListener { listener.onChange() }
        }
    }
}
