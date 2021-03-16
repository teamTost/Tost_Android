package com.tost.presentation.mypage.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.tost.R
import com.tost.databinding.DialogSimpleAlertBinding

/**
 * Created By Malibin
 * on 3월 16, 2021
 */

abstract class SimpleAlertDialog(context: Context) : Dialog(context) {
    abstract val contentText: String
    abstract val buttonText: String
    private var onButtonClickListener: View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogSimpleAlertBinding.inflate(layoutInflater).apply {
            buttonClose.setOnClickListener { dismiss() }
            textContent.text = contentText
            button.text = buttonText
            button.setOnClickListener(onButtonClickListener)
        }
        setContentView(binding.root)
        setTransparentWindowBackground()
    }

    private fun setTransparentWindowBackground() {
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun setOnButtonClickListener(listener: View.OnClickListener?) {
        this.onButtonClickListener = View.OnClickListener {
            listener?.onClick(it)
            dismiss()
        }
    }
}
