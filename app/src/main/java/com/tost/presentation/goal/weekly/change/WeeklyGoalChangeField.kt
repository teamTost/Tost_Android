package com.tost.presentation.goal.weekly.change

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
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

    fun getCountToChange(): String {
        return binding.textCountToChange.text.toString()
    }

    fun addTextChangedListener(listener: (text: Editable?) -> Unit) {
        binding.textCountToChange.addTextChangedListener {
            listener(it)
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("countText")
        fun bindingCountText(view: WeeklyGoalChangeField, text: String?) {
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "app:countText", event = "countTextAttrChanged")
        fun getCountText(view: WeeklyGoalChangeField): String {
            return view.getCountToChange()
        }

        @JvmStatic
        @BindingAdapter("countTextAttrChanged")
        fun setCountTextInverseListener(
            view: WeeklyGoalChangeField,
            listener: InverseBindingListener
        ) {
            view.addTextChangedListener { listener.onChange() }
        }
    }
}