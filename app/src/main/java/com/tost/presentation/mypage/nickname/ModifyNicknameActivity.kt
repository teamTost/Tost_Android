package com.tost.presentation.mypage.nickname

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tost.databinding.ActivityModifyNicknameBinding
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Malibin
 * on 3월 21, 2021
 */

@AndroidEntryPoint
class ModifyNicknameActivity : AppCompatActivity() {

    private val modifyNicknameViewModel: ModifyNicknameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityModifyNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = modifyNicknameViewModel

        modifyNicknameViewModel.isSuccess.observe(this) {
            if (it) finish()
        }
        modifyNicknameViewModel.toastMessage.observe(this) {
            showToast(it)
        }
    }
}
