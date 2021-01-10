package com.tost.presentation.problem.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.tost.R
import com.tost.databinding.DialogStopTalkingPauseBinding
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

/**
 * Created By Malibin
 * on 1월 10, 2020
 */

class StopTalkingPauseDialog(context: Context) : Dialog(context) {
    private var onFinishListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogStopTalkingPauseBinding.inflate(layoutInflater)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(binding.root)
        setTransparentWindowBackground()

        CoroutineScope(Dispatchers.Main).launch {
            delay(PAUSE_TIME)
            onFinishListener?.invoke()
            dismiss()
        }
    }

    private fun setTransparentWindowBackground() {
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun setOnFinishListener(listener: (() -> Unit)?) {
        this.onFinishListener = listener
    }

    companion object {
        private val PAUSE_TIME = TimeUnit.SECONDS.toMillis(3)
    }
}
