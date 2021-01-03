package com.tost.presentation.problem.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.tost.R
import com.tost.databinding.DialogNextOrReviewProblemBinding

/**
 * Created By Malibin
 * on 1월 03, 2021
 */

class NextOrReviewProblemDialog(context: Context) : Dialog(context) {

    private var onReviewClickListener: View.OnClickListener? = null
    private var onNextClickListener: View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogNextOrReviewProblemBinding.inflate(layoutInflater).apply {
            buttonReview.setOnClickListener(onReviewClickListener)
            buttonNext.setOnClickListener(onNextClickListener)
        }
        setContentView(binding.root)
        setTransparentWindowBackground()
    }

    private fun setTransparentWindowBackground() {
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun setOnCheckProblemClickListener(listener: View.OnClickListener?) {
        this.onReviewClickListener = View.OnClickListener {
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
