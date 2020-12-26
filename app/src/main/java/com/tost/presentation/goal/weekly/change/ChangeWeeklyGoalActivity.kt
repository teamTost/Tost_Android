package com.tost.presentation.goal.weekly.change

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tost.databinding.ActivityChangeWeeklyGoalBinding

class ChangeWeeklyGoalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChangeWeeklyGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}