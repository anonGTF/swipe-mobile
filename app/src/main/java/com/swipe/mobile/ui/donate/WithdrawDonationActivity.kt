package com.swipe.mobile.ui.donate

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.data.model.BaseResponse
import com.swipe.mobile.databinding.ActivityWithdrawDonationBinding
import com.swipe.mobile.ui.documentsigning.DocumentSigningActivity
import com.swipe.mobile.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawDonationActivity : BaseActivity<ActivityWithdrawDonationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityWithdrawDonationBinding =
        ActivityWithdrawDonationBinding::inflate

    private val viewModel: DonateViewModel by viewModels()
    private var id: Int = 0

    override fun setup() {
        id = intent.getIntExtra(EXTRA_ID, 0)
        binding.btnWithdraw.setOnClickListener {
            viewModel.withdraw(id).observe(
                this, handleWithdraw()
            )
        }
        binding.btnSign.setOnClickListener {
            goToActivity(DocumentSigningActivity::class.java)
        }
    }

    private fun handleWithdraw() = setObserver<BaseResponse<String>>(
        onSuccess = {
            showToast("Withdraw success")
            goToActivity(MainActivity::class.java, null, clearIntent = true, isFinish = true)
        }
    )

    companion object {
        const val EXTRA_ID = "id"
    }
}