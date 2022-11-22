package com.swipe.mobile.ui.donate

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.data.model.BaseResponse
import com.swipe.mobile.data.model.Donation
import com.swipe.mobile.databinding.ActivityCreateDonationBinding
import com.swipe.mobile.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateDonationActivity : BaseActivity<ActivityCreateDonationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCreateDonationBinding =
        ActivityCreateDonationBinding::inflate

    private val viewModel: DonateViewModel by viewModels()
    private var id: Int = 0

    override fun setup() {
        id = intent.getIntExtra(EXTRA_ID, 0)
        binding.btnDonate.setOnClickListener {
            viewModel.donate(id, binding.etTitle.text.toString().toInt()).observe(
                this, handleDonate()
            )
        }
    }

    private fun handleDonate() = setObserver<BaseResponse<Donation>>(
        onSuccess = {
            showToast("Donation success")
            goToActivity(MainActivity::class.java, null, clearIntent = true, isFinish = true)
        }
    )

    companion object {
        const val EXTRA_ID = "id"
    }
}