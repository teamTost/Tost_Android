package com.tost.presentation.problem.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.tost.R
import com.tost.databinding.WidgetProblemToolbarBinding

/**
 * Created By Malibin
 * on 12월 03, 2020
 */

class ProblemToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = WidgetProblemToolbarBinding.inflate(LayoutInflater.from(context), this)

    init {
        context.withStyledAttributes(attrs, R.styleable.ProblemToolBar) { applyAttributes(this) }
    }

    private fun applyAttributes(typedArray: TypedArray) {
        binding.textTitle.text = typedArray.getString(R.styleable.ProblemToolBar_title)
    }

    fun setTitle(text: CharSequence) {
        binding.textTitle.text = text
    }

    fun setOnCloseClickListener(runBlock: ((View) -> Unit)?) {
        binding.buttonClose.setOnClickListener(runBlock)
    }

    fun setOnBookmarkClickListener(runBlock: ((View) -> Unit)?) {
        binding.buttonBookmark.setOnClickListener(runBlock)
    }
}
