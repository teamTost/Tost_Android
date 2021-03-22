package com.tost.presentation.nickname.modify

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tost.R
import com.tost.databinding.ActivityModifyNicknameBinding
import com.tost.presentation.nickname.NicknameViewModel
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Malibin
 * on 3월 21, 2021
 */

@AndroidEntryPoint
class ModifyNicknameActivity : AppCompatActivity() {

    private val nicknameViewModel: NicknameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityModifyNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = nicknameViewModel
        window.statusBarColor = ContextCompat.getColor(this, R.color.cloudy_orange)

        nicknameViewModel.isSuccess.observe(this) {
            if (it) finish()
        }
        nicknameViewModel.toastMessage.observe(this) {
            showToast(it)
        }
    }
}
