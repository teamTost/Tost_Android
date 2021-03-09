package com.tost.presentation.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tost.databinding.ActivityMyPageBinding

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
