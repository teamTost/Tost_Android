package com.tost.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tost.R
import com.tost.databinding.ActivityMyPageBinding
import com.tost.presentation.goal.EntireGoalActivity
import com.tost.presentation.mypage.dialog.LogoutAlertDialog
import com.tost.presentation.mypage.dialog.MemberWithdrawalAlertDialog
import com.tost.presentation.mypage.nickname.ModifyNicknameActivity
import com.tost.presentation.utils.showToast
import com.tost.presentation.utils.showTostToast
import com.tost.presentation.web.WebActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {

    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = myPageViewModel
        binding.lifecycleOwner = this
        window.statusBarColor = ContextCompat.getColor(this, R.color.cloudy_orange)
    }

    override fun onResume() {
        super.onResume()

        myPageViewModel.loadUserInfo()
    }

    fun launchModifyEntireGoalActivity(view: View) {
        val intent = Intent(this, EntireGoalActivity::class.java)
        intent.putExtra(EntireGoalActivity.KEY_IS_MODIFY, true)
        startActivity(intent)
    }

    fun launchModifyNicknameActivity(view: View) {
        val intent = Intent(this, ModifyNicknameActivity::class.java)
        startActivity(intent)
    }

    fun launchTermsAndConditionsOfServiceActivity(view: View) {
        launchWebActivityWith(URL_TERMS_AND_CONDITIONS_OF_SERVICE)
    }

    fun launchPrivacyPolicyActivity(view: View) {
        launchWebActivityWith(URL_PRIVACY_POLICY)
    }

    private fun launchWebActivityWith(url: String) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(WebActivity.WEB_URL_KEY, url)
        startActivity(intent)
    }

    fun showLogoutAlertDialog(view: View) {
        LogoutAlertDialog(this).apply {
            setOnButtonClickListener { showToast("로그아웃햇넹") }
        }.show()
    }

    fun showMemberWithdrawalAlertDialog(view: View) {
        MemberWithdrawalAlertDialog(this).apply {
            setOnButtonClickListener { showToast("회원탈퇴햇넹 ㅠ") }
        }.show()
    }

    companion object {
        private const val URL_TERMS_AND_CONDITIONS_OF_SERVICE =
            "https://www.notion.so/cbeaffd2ab3a4064b93876c00e98c025"
        private const val URL_PRIVACY_POLICY =
            "https://www.notion.so/2cdcb359b69645bbbacc0816810529c1"
    }
}
