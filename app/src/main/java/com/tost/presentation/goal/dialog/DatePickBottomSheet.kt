package com.tost.presentation.goal.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tost.R
import com.tost.databinding.BottomSheetDateSelectBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By Malibin
 * on 12월 23, 2020
 */

class DatePickBottomSheet : BottomSheetDialogFragment() {

    private var onDatePickListener: OnDatePickListener? = null

    override fun getTheme() = R.style.Widget_AppTheme_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetDateSelectBinding.inflate(layoutInflater, container, false)
        initView(binding)
        return binding.root
    }

    private fun initView(binding: BottomSheetDateSelectBinding) {
        binding.buttonSubmit.setOnClickListener { onSubmitClick(getPickedDate(binding)) }
    }

    private fun onSubmitClick(date: Date) {
        onDatePickListener?.onDatePick(date)
        dismiss()
    }

    private fun getPickedDate(binding: BottomSheetDateSelectBinding): Date {
        val day = NUMBER_FORMAT.format(binding.pickerDate.dayOfMonth)
        val month = NUMBER_FORMAT.format(binding.pickerDate.month + 1)
        val year = binding.pickerDate.year.toString()
        val format = SimpleDateFormat(DATE_FORMAT, Locale.KOREA)
        return format.parse("$year$month$day")
            ?: throw IllegalArgumentException("Cannot convert date")
    }

    fun setOnDatePickListener(listener: OnDatePickListener?) {
        this.onDatePickListener = listener
    }

    companion object {
        private const val NUMBER_FORMAT = "%02d"
        private const val DATE_FORMAT = "yyyyMMdd"
    }

    fun interface OnDatePickListener {
        fun onDatePick(date: Date)
    }
}
