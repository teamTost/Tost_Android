package com.tost.presentation.problem.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.withStyledAttributes
import com.tost.R

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

class AudioStateButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var state: State = State.RECORDING
        set(value) {
            changeDrawable(state)
            field = value
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.AudioStateButton) { applyAttributes(this) }
    }

    private fun applyAttributes(typedArray: TypedArray) {
        val id = typedArray.getInt(R.styleable.AudioStateButton_state, 0)
        val state = State.findBy(id)
        changeDrawable(state)
    }

    private fun changeDrawable(state: State) {
        this.setImageResource(state.imageRes)
    }

    fun setOnStateClickListener(listener: OnClickListener) {
        this.setOnClickListener { listener.onAudioButtonClick(state) }
    }

    fun interface OnClickListener {
        fun onAudioButtonClick(state: State)
    }

    enum class State(
        val id: Int,
        @DrawableRes val imageRes: Int,
    ) {
        RECORDING(0, R.drawable.responsetime_btn_stop),
        STOP(1, R.drawable.responsetime_btn_record),
        PLAYING(2, R.drawable.myrecord_btn_stop),
        PAUSE(3, R.drawable.myrecord_btn_play);

        companion object {
            fun findBy(id: Int): State = values().find { it.id == id }
                ?: throw IllegalArgumentException("not exist State of $id")
        }
    }
}
