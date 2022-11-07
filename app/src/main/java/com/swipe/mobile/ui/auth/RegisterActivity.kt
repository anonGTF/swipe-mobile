package com.swipe.mobile.ui.auth

import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityRegisterBinding =
        ActivityRegisterBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}