package com.tost.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tost.R
import com.tost.databinding.ActivityMyPageBinding
import com.tost.presentation.auth.LoginActivity
import com.tost.presentation.goal.EntireGoalActivity
import com.tost.presentation.mypage.dialog.LogoutAlertDialog
import com.tost.presentation.mypage.dialog.MemberWithdrawalAlertDialog
import com.tost.presentation.nickname.modify.ModifyNicknameActivity
import com.tost.presentation.web.WebActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {

    private val myPageViewModel: MyPageViewModel by viewModels()
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = myPageViewModel
        binding.lifecycleOwner = this
        window.statusBarColor = ContextCompat.getColor(this, R.color.cloudy_orange)

        googleSignInClient = getGoogleSignInClient()

        myPageViewModel.isDataCleared.observe(this) {
            if (it) goLoginActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        myPageViewModel.loadUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        googleSignInClient = null
    }

    private fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
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
        val url = resources.getString(R.string.url_terms_and_conditions_of_service)
        launchWebActivityWith(url)
    }

    fun launchPrivacyPolicyActivity(view: View) {
        val url = resources.getString(R.string.url_privacy_policy)
        launchWebActivityWith(url)
    }

    private fun launchWebActivityWith(url: String) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(WebActivity.WEB_URL_KEY, url)
        startActivity(intent)
    }

    fun showLogoutAlertDialog(view: View) {
        LogoutAlertDialog(this).apply {
            setOnButtonClickListener { myPageViewModel.logout() }
        }.show()
    }

    fun showMemberWithdrawalAlertDialog(view: View) {
        MemberWithdrawalAlertDialog(this).apply {
            setOnButtonClickListener { myPageViewModel.quitMember() }
        }.show()
    }

    private fun goLoginActivity() {
        googleSignInClient?.signOut()
        if (myPageViewModel.isUserQuit) googleSignInClient?.revokeAccess()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
