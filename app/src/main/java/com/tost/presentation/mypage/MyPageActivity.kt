package com.tost.presentation.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tost.databinding.ActivityMyPageBinding
import com.tost.presentation.web.WebActivity

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    companion object {
        private const val URL_TERMS_AND_CONDITIONS_OF_SERVICE =
            "https://www.notion.so/cbeaffd2ab3a4064b93876c00e98c025"
        private const val URL_PRIVACY_POLICY =
            "https://www.notion.so/2cdcb359b69645bbbacc0816810529c1"
    }
}
