package com.tost.presentation.problem.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.tost.R
import com.tost.databinding.WidgetTostProgressBarBinding
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

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

    private var duration = 0
    private var progress = 0
    private var isDragging = false
    private var isReverse = false
    private var progressJob: Job? = null
    private var onProgressChangeListener: OnProgressChangeListener? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.TostTimer) { applyAttributes(this) }
        binding.seekBar.setOnSeekBarChangeListener(this)
    }

    private fun applyAttributes(typedArray: TypedArray) {
        isReverse = typedArray.getBoolean(R.styleable.TostTimer_reverse, false)
        val duration = typedArray.getInt(R.styleable.TostTimer_duration_millis, 0)
        if (isReverse) initToTimer(duration)
        else initToProgressBar(duration)
    }

    fun initToTimer(duration: Int) {
        isReverse = true
        this.duration = duration
        binding.textLeftSeconds.text = duration.toSecondsText()
        binding.textRightSeconds.text = INITIAL_SECONDS.toSecondsText()
        binding.seekBar.rotation = 180f
        binding.seekBar.progress = duration
        binding.seekBar.setOnTouchListener { _, _ -> true }
    }

    fun initToProgressBar(duration: Int) {
        isReverse = false
        this.duration = duration
        binding.textLeftSeconds.text = INITIAL_SECONDS.toSecondsText()
        binding.textRightSeconds.text = duration.toSecondsText()
        binding.seekBar.rotation = 0f
        binding.seekBar.setOnTouchListener(null)
    }

    private fun Int.toSecondsText(): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this.toLong())
        return context.resources.getString(R.string.n_seconds, seconds)
    }

    fun start() {
        progressJob?.cancel()
        binding.seekBar.max = duration
        progress = if (isReverse) duration else 0
        progressJob = startProgressJob(duration)
    }

    private fun startProgressJob(duration: Int) = CoroutineScope(Dispatchers.IO).launch {
        val tick = if (isReverse) -duration.calculateTick() else duration.calculateTick()
        while (isProgressing(duration)) {
            delay(abs(tick).toLong())
            progress += tick
            withContext(Dispatchers.Main) { if (!isDragging) binding.seekBar.progress = progress }
        }
    }

    private fun Int.calculateTick(): Int = this shr 10 + 30

    private fun isProgressing(duration: Int): Boolean {
        return if (isReverse) progress > 0
        else progress < duration
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
        progress = seekBar?.progress ?: return
        if (progressJob?.isActive == false) {
            progressJob = startProgressJob(seekBar.max)
        }
        onProgressChangeListener?.onStopTrackingTouch(progress)
    }

    fun setOnProgressChangeListener(l: OnProgressChangeListener?) {
        this.onProgressChangeListener = l
    }

    companion object {
        private const val INITIAL_SECONDS = 0
    }

    fun interface OnProgressChangeListener {
        fun onStopTrackingTouch(currentProgress: Int)
    }
}
//TODO 시간끝났을 때 리스너 달아주기
// 깜빡거리는 버그 고치기
