package com.tost.presentation.problem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.firebase.messaging.FirebaseMessaging
import com.tost.data.entity.Part
import com.tost.databinding.ActivityProblemEntryBinding
import com.tost.presentation.problem.dialog.NextOrReviewProblemDialog
import com.tost.presentation.problem.dialog.StopTalkingButtonsDialog
import com.tost.presentation.utils.printLog
import com.tost.presentation.utils.showToast
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
        binding.buttonStart.setOnClickListener { onStartClick() }
        binding.viewModel = problemEntryViewModel
        binding.lifecycleOwner = this
        problemEntryViewModel.loadMyNote(part)
    }

    private fun getPart(): Part = intent.getSerializableExtra(KEY_PART) as? Part
        ?: throw IllegalArgumentException("part must be send")

    private fun onStartClick() {
        if (problemEntryViewModel.isMyNoteNotNull()) {
            deployNextOrReviewProblemDialog()
            return
        }
        deployProblemPrepareActivity()
    }

    private fun deployProblemPrepareActivity() {
        val intent = Intent(this, ProblemGuideActivity::class.java)
        intent.putExtra(ProblemGuideActivity.KEY_PART, getPart())
        intent.putExtra(
            ProblemGuideActivity.KEY_PROBLEM_NUMBER,
            problemEntryViewModel.getNextProblemNumber()
        )
        startActivity(intent)
    }

    private fun deployNextOrReviewProblemDialog() {
        NextOrReviewProblemDialog(this).apply {
            setOnCheckProblemClickListener { showToast("복습하기 액티비티 띄우기") }
            setOnNextClickListener { deployProblemPrepareActivity() }
        }.show()
    }

    companion object {
        const val KEY_PART = "part"
    }
}
