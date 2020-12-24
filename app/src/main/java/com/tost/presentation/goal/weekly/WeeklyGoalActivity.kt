package com.tost.presentation.goal.weekly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tost.databinding.ActivityWeeklyGoalBinding

class WeeklyGoalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityWeeklyGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}