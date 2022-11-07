package com.swipe.mobile.ui.auth

import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityLandingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingActivity : BaseActivity<ActivityLandingBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityLandingBinding =
        ActivityLandingBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}