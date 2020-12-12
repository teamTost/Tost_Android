package com.tost.presentation.problem.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.databinding.BindingAdapter
import com.tost.R
import com.tost.databinding.WidgetTostProgressBarBinding
import java.util.concurrent.TimeUnit

/**
 * Created By Malibin
 * on 12월 03, 2020
 */

class TostProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), SeekBar.OnSeekBarChangeListener {

    private val binding = WidgetTostProgressBarBinding.inflate(LayoutInflater.from(context), this)
    private var onProgressChangeListener: OnProgressChangeListener? = null
    private var onProgressFinishListener: OnProgressFinishListener? = null

    private var isDragging = false
    private var isReverse = false

    var maxProgress: Int = binding.seekBar.max
    var progress: Int
        get() = binding.seekBar.progress
        set(value) {
            setProgressSeekBar(value)
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.TostTimer) { applyAttributes(this) }
        binding.seekBar.setOnSeekBarChangeListener(this)
    }

    private fun applyAttributes(typedArray: TypedArray) {
        isReverse = typedArray.getBoolean(R.styleable.TostTimer_reverse, false)
        val maxProgress = typedArray.getInt(R.styleable.TostTimer_duration_millis, 0)
        if (isReverse) initToTimer(maxProgress)
        else initToProgressBar(maxProgress)
    }

    fun initToTimer(maxProgress: Int) {
        this.isReverse = true
        this.maxProgress = maxProgress
        binding.seekBar.max = maxProgress
        binding.textLeftSeconds.text = maxProgress.toSecondsText()
        binding.textRightSeconds.text = INITIAL_SECONDS.toSecondsText()
        binding.seekBar.rotation = 180f
        binding.seekBar.progress = maxProgress
        binding.seekBar.setOnTouchListener { _, _ -> true }
    }

    fun initToProgressBar(maxProgress: Int) {
        this.isReverse = false
        this.maxProgress = maxProgress
        binding.seekBar.max = maxProgress
        binding.textLeftSeconds.text = INITIAL_SECONDS.toSecondsText()
        binding.textRightSeconds.text = maxProgress.toSecondsText()
        binding.seekBar.rotation = 0f
        binding.seekBar.setOnTouchListener(null)
    }

    private fun Int.toSecondsText(): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this.toLong())
        return context.resources.getString(R.string.n_seconds, seconds)
    }

    private fun setProgressSeekBar(input: Int) {
        if (isDragging) return
        val progress = if (isReverse) maxProgress - input else input
        binding.seekBar.progress = progress
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        binding.textProgressSeconds.text = progress.toSecondsText()
        binding.textProgressSeconds.translationX = getProgressPosition()
    }

    private fun getProgressPosition(): Float {
        return if (isReverse) binding.seekBar.width - binding.seekBar.thumb.bounds.left.toFloat()
        else binding.seekBar.thumb.bounds.left.toFloat()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        isDragging = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        isDragging = false
        onProgressChangeListener?.onStopTrackingTouch(seekBar?.progress ?: return)
    }

    fun setOnProgressChangeListener(l: OnProgressChangeListener?) {
        this.onProgressChangeListener = l
    }

    fun setOnProgressFinishListener(l: OnProgressFinishListener?) {
        this.onProgressFinishListener = l
    }

    companion object {
        private const val INITIAL_SECONDS = 0
    }

    fun interface OnProgressChangeListener {
        fun onStopTrackingTouch(currentProgress: Int)
    }

    fun interface OnProgressFinishListener {
        fun onFinish()
    }
}
