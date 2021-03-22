package com.tost.presentation.nickname.init

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tost.R
import com.tost.databinding.ActivityInitNicknameBinding
import com.tost.presentation.mypage.MyPageActivity
import com.tost.presentation.nickname.NicknameViewModel
import com.tost.presentation.utils.showToast
import com.tost.presentation.web.WebActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitNicknameActivity : AppCompatActivity() {

    private val nicknameViewModel: NicknameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityInitNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = nicknameViewModel
        binding.lifecycleOwner = this

        window.statusBarColor = ContextCompat.getColor(this, R.color.cloudy_orange)

        nicknameViewModel.isSuccess.observe(this) {
            if (it) finish()
        }
        nicknameViewModel.toastMessage.observe(this) {
            showToast(it)
        }
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
}
