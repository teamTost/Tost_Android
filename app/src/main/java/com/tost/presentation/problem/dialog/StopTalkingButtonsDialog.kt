package com.tost.presentation.problem.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.tost.R
import com.tost.databinding.DialogStopTalkingButtonsBinding

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class StopTalkingButtonsDialog(context: Context) : Dialog(context) {

    private var onCheckProblemClickListener: View.OnClickListener? = null
    private var onNextClickListener: View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogStopTalkingButtonsBinding.inflate(layoutInflater).apply {
            buttonCheck.setOnClickListener(onCheckProblemClickListener)
            buttonNext.setOnClickListener(onNextClickListener)
        }
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(binding.root)
        setTransparentWindowBackground()
    }

    private fun setTransparentWindowBackground() {
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun setOnCheckProblemClickListener(listener: View.OnClickListener?) {
        this.onCheckProblemClickListener = View.OnClickListener {
            listener?.onClick(it)
            dismiss()
        }
    }

    fun setOnNextClickListener(listener: View.OnClickListener?) {
        this.onNextClickListener = View.OnClickListener {
            listener?.onClick(it)
            dismiss()
        }
    }
}
