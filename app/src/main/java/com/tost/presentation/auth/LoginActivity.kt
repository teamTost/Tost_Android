package com.tost.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tost.R
import com.tost.databinding.ActivityLoginBinding
import com.tost.presentation.home.HomeActivity
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView(binding)
        subscribeViewModel()

        loginViewModel.runAutoLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.handleActivityResult(requestCode, resultCode, data)
    }

    private fun initView(binding: ActivityLoginBinding) {
        binding.lifecycleOwner = this
        binding.viewModel = loginViewModel
        binding.buttonLogin.setOnClickListener { deployGoogleAuth(createGoogleSignInOptions()) }
        binding.buttonTry.setOnClickListener { deployHomeActivity(true) }
    }

    private fun subscribeViewModel() {
        loginViewModel.toastMessage.observe(this) { showToast(it) }
        loginViewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                deployHomeActivity(); finish()
            }
        }
    }

    private fun deployHomeActivity(isTrial: Boolean = false) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(HomeActivity.KEY_TRIAL, isTrial)
        startActivity(intent)
    }

    private fun deployGoogleAuth(gso: GoogleSignInOptions) {
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, REQUEST_CODE_GOOGLE_AUTH)
    }

    private fun createGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .requestEmail()
            .build()
    }

    companion object {
        const val REQUEST_CODE_GOOGLE_AUTH = 5000
    }
}
