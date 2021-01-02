package com.tost.presentation.problem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.firebase.messaging.FirebaseMessaging
import com.tost.data.entity.Part
import com.tost.databinding.ActivityProblemEntryBinding
import com.tost.presentation.utils.printLog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProblemEntryActivity : AppCompatActivity() {

    private val problemEntryViewModel: ProblemEntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityProblemEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView(binding)
    }

    private fun initView(binding: ActivityProblemEntryBinding) {
        val part = getPart()
        binding.part = part
        binding.buttonStart.setOnClickListener { deployProblemPrepareActivity() }
        binding.viewModel = problemEntryViewModel
        binding.lifecycleOwner = this
        problemEntryViewModel.loadMyNote(part)
    }

    private fun getPart(): Part = intent.getSerializableExtra(KEY_PART) as? Part
        ?: throw IllegalArgumentException("part must be send")

    private fun deployProblemPrepareActivity() {
        val intent = Intent(this, ProblemGuideActivity::class.java)
        intent.putExtra(ProblemGuideActivity.KEY_PART, getPart())
        intent.putExtra(
            ProblemGuideActivity.KEY_PROBLEM_NUMBER,
            problemEntryViewModel.getNextProblemNumber()
        )
        startActivity(intent)
    }

    companion object {
        const val KEY_PART = "part"
    }
}
