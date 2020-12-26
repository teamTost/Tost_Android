package com.tost.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tost.data.entity.Part
import com.tost.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView(binding)
        homeViewModel.loadGoals(Date())
    }

    private fun initView(binding: ActivityHomeBinding) {
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
        binding.listPractices.adapter = HomePracticesAdapter(Part.values())
        binding.seekBar.setOnTouchListener { _, _ -> true }
    }
}
