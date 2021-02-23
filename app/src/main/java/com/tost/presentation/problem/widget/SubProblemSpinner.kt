package com.tost.presentation.problem.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.tost.R
import com.tost.data.entity.Problem

/**
 * Created By Malibin
 * on 2월 23, 2021
 */

class SubProblemSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatSpinner(context, attrs, defStyleAttr) {

    var questionNumber = 1
        set(value) {
            field = value
            (adapter as SpinnerAdapter).resetItems()
        }

    init {
        adapter = SpinnerAdapter()
    }

    fun setOnItemSelectedListener(listener: SubProblemSpinnerClickListener) {
        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                listener.onItemClick(Problem.SubNumber.values()[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                /* Nothing Happened */
            }
        }
    }

    private inner class SpinnerAdapter : ArrayAdapter<String>(
        this.context,
        R.layout.item_sub_problem_number_with_icon,
        getSpinnerItems(),
    ) {
        init {
            setDropDownViewResource(R.layout.item_sub_problem_number)
        }

        fun resetItems() {
            clear()
            addAll(getSpinnerItems())
        }
    }

    private fun getSpinnerItems(): List<String> = listOf(
        "Q $questionNumber-1",
        "Q $questionNumber-2",
        "Q $questionNumber-3"
    )

    fun interface SubProblemSpinnerClickListener {
        fun onItemClick(subNumber: Problem.SubNumber)
    }
}
