package com.tost.presentation.goal.dialog

import android.app.Activity
import android.content.Intent
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

    private fun getPickedDate(binding: BottomSheetDateSelectBinding): Date {
        val day = NUMBER_FORMAT.format(binding.pickerDate.dayOfMonth)
        val month = NUMBER_FORMAT.format(binding.pickerDate.month + 1)
        val year = binding.pickerDate.year.toString()
        val format = SimpleDateFormat(DATE_FORMAT, Locale.KOREA)
        return format.parse("$year$month$day")
            ?: throw IllegalArgumentException("Cannot convert date")
    }

    private fun onSubmitClick(date: Date) {
        val intent = Intent()
        intent.putExtra(KEY_DATE, date)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        dismiss()
    }

    companion object {
        const val REQUEST_CODE = 2000
        const val KEY_DATE = "date"

        private const val NUMBER_FORMAT = "%02d"
        private const val DATE_FORMAT = "yyyyMMdd"
    }
}
