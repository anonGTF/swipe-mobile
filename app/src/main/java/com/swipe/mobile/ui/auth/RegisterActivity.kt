package com.swipe.mobile.ui.auth

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.data.model.AuthResponse
import com.swipe.mobile.databinding.ActivityRegisterBinding
import com.swipe.mobile.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityRegisterBinding =
        ActivityRegisterBinding::inflate

    private val viewModel: AuthViewModel by viewModels()

    override fun setup() {
        with(binding) {
            btnRegister.setOnClickListener {
                viewModel.register(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString()
                ).observe(this@RegisterActivity, handleRegister())
            }
        }
    }

    private fun handleRegister() = setObserver<AuthResponse>(
        onSuccess = { viewModel.login(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        ).observe(this, handleLogin()) }
    )

    private fun handleLogin() = setObserver<AuthResponse>(
        onSuccess = { goToActivity(
            MainActivity::class.java, null,
            clearIntent = true,
            isFinish = true
        ) }
    )
}