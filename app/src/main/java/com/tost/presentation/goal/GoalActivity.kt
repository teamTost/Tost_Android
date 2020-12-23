package com.tost.presentation.goal

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tost.R
import com.tost.presentation.goal.dialog.DatePickBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class GoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)

        deployDatePicker()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        val date = data?.getSerializableExtra(DatePickBottomSheet.KEY_DATE) as Date
//        findViewById<TextView>(R.id.text_goal_date)
    }

    private fun deployDatePicker() {
        DatePickBottomSheet()
            .show(supportFragmentManager, "dialog")
    }
}