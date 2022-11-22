package com.swipe.mobile.ui.auth

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.data.model.AuthResponse
import com.swipe.mobile.databinding.ActivityLoginBinding
import com.swipe.mobile.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding =
        ActivityLoginBinding::inflate

    private val viewModel: AuthViewModel by viewModels()
    private var isCommunity = false

    override fun setup() {
        isCommunity = intent.getBooleanExtra(IS_COMMUNITY, false)
        with(binding) {
            btnLogin.setOnClickListener {
                if (isCommunity) {
                    viewModel.loginCommunity(etEmail.text.toString(), etPassword.text.toString())
                        .observe(this@LoginActivity, handleLogin())
                } else {
                    viewModel.login(etEmail.text.toString(), etPassword.text.toString())
                        .observe(this@LoginActivity, handleLogin())
                }
            }
        }
    }

    private fun handleLogin() = setObserver<AuthResponse>(
        onSuccess = { goToActivity(MainActivity::class.java, null,
            clearIntent = true,
            isFinish = true
        ) }
    )

    companion object {
        const val IS_COMMUNITY = "is community"
    }
}