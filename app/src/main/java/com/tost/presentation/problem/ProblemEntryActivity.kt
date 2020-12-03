package com.tost.presentation.problem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.tost.data.entity.Part
import com.tost.databinding.ActivityProblemEntryBinding
import com.tost.presentation.utils.printLog

// TODO: 넘겨진 파트에 따라 다음 액티비티 띄우는 분기처리
// TODO: 넘겨진 파트에 따라 목표 서버 데이터 분기처리
// TODO: SKIP 버튼 커스텀뷰로 옮길까..
class ProblemEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityProblemEntryBinding.inflate(layoutInflater)
        binding.part = getPart()
//        binding.mynote = MyNote(
//            solvedProblemCount = 12,
//            remainProblemCount = 18,
//            wholeProblemCount = 30,
//            weeklyGoalRatio = 60,
//            weeklySolvedProblemCount = 12,
//            weeklyGoalCount = 20,
//        )
        binding.buttonStart.setOnClickListener { deployProblemPrepareActivity() }
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { printLog("fcm token : ${it.result}") }
    }

    private fun getPart(): Part = intent.getSerializableExtra(KEY_PART) as? Part
        ?: Part.FIVE
        ?: throw IllegalArgumentException("part must be send")
    // TODO: 디폴트값 지우기

    private fun deployProblemPrepareActivity() {
        val intent = Intent(this, ProblemGuideActivity::class.java)
        intent.putExtra(ProblemGuideActivity.KEY_PART, getPart())
        startActivity(intent)
    }

    companion object {
        const val KEY_PART = "part"
    }
}