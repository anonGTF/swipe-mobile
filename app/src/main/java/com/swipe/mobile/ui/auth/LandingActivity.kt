package com.swipe.mobile.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityLandingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingActivity : BaseActivity<ActivityLandingBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityLandingBinding =
        ActivityLandingBinding::inflate

    override fun setup() {
        with(binding) {
            btnLogin.setOnClickListener {
                val bundle = Bundle().apply {
                    putBoolean(LoginActivity.IS_COMMUNITY, false)
                }
                goToActivity(LoginActivity::class.java, bundle, clearIntent = true, isFinish = true)
            }

            btnLoginCommunity.setOnClickListener {
                val bundle = Bundle().apply {
                    putBoolean(LoginActivity.IS_COMMUNITY, true)
                }
                goToActivity(LoginActivity::class.java, bundle, clearIntent = true, isFinish = true)
            }

            btnRegister.setOnClickListener {
                goToActivity(RegisterActivity::class.java)
            }
        }
    }
}