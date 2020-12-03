package com.tost.presentation.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.tost.R
import com.tost.databinding.WidgetTostToastBinding

/**
 * Created By Malibin
 * on 12월 03, 2020
 */

fun Context.showTostToast(message: String) {
    val binding = WidgetTostToastBinding.inflate(LayoutInflater.from(this))
    binding.textMessage.text = message
    val topMargin = resources.getDimensionPixelOffset(R.dimen.tost_toast_top_margin)
    Toast(this).apply {
        view = binding.root
        duration = Toast.LENGTH_SHORT
        setGravity(Gravity.TOP, 0, topMargin)
    }.show()
}