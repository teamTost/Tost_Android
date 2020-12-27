package com.tost.presentation.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tost.R
import com.tost.databinding.ActivityTestBinding
import com.tost.presentation.utils.showToast

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonTest2.setOnClickListener { showToast(R.string.to_be_continued) }
        binding.buttonTest3.setOnClickListener { showToast(R.string.to_be_continued) }
    }
}
