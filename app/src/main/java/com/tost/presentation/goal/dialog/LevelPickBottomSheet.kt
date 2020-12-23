package com.tost.presentation.goal.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tost.R
import com.tost.databinding.BottomSheetLevelSelectBinding

/**
 * Created By Malibin
 * on 12월 23, 2020
 */

class LevelPickBottomSheet : BottomSheetDialogFragment() {

    private var onLevelPickListener: OnLevelPickListener? = null

    override fun getTheme() = R.style.Widget_AppTheme_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetLevelSelectBinding.inflate(layoutInflater, container, false)
        initView(binding)
        return binding.root
    }

    private fun initView(binding: BottomSheetLevelSelectBinding) {
        binding.buttonSubmit.setOnClickListener { onSubmitClick(getPickedLevel(binding)) }
        binding.pickerLevel.minValue = 0
        binding.pickerLevel.maxValue = LEVELS.size - 1
        binding.pickerLevel.displayedValues = LEVELS
    }

    private fun getPickedLevel(binding: BottomSheetLevelSelectBinding): Int {
        return binding.pickerLevel.value + 1
    }

    private fun onSubmitClick(level: Int) {
        onLevelPickListener?.onLevelPick(level)
        dismiss()
    }

    fun setOnLevelPickListener(listener: OnLevelPickListener?) {
        this.onLevelPickListener = listener
    }

    fun interface OnLevelPickListener {
        fun onLevelPick(level: Int)
    }

    companion object{
        private val LEVELS = arrayOf(
            "Level 1",
            "Level 2",
            "Level 3",
            "Level 4",
            "Level 5",
            "Level 6",
            "Level 7",
            "Level 8"
        )
    }
}
