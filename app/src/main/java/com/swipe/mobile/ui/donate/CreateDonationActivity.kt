package com.swipe.mobile.ui.donate

import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityCreateDonationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateDonationActivity : BaseActivity<ActivityCreateDonationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCreateDonationBinding =
        ActivityCreateDonationBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}