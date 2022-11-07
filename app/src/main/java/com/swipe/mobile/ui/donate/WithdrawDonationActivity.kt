package com.swipe.mobile.ui.donate

import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityWithdrawDonationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawDonationActivity : BaseActivity<ActivityWithdrawDonationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityWithdrawDonationBinding =
        ActivityWithdrawDonationBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}