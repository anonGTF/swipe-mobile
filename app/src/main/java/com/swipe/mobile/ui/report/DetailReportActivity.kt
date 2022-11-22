package com.swipe.mobile.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.data.model.BaseResponse
import com.swipe.mobile.data.model.Report
import com.swipe.mobile.databinding.ActivityDetailReportBinding
import com.swipe.mobile.ui.donate.CreateDonationActivity
import com.swipe.mobile.ui.donate.WithdrawDonationActivity
import com.swipe.mobile.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReportActivity : BaseActivity<ActivityDetailReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDetailReportBinding =
        ActivityDetailReportBinding::inflate

    private val adapter: ReportImageAdapter by lazy { ReportImageAdapter(this) }
    private val viewModel: ReportViewModel by viewModels()
    private var report: Report? = null

    override fun setup() {
        report = intent.getParcelableExtra(EXTRA_DATA)
        with(binding) {
            tvTitle.text = report?.title
            tvDesc.text = report?.desc
            tvLocation.text = report?.location

            imageSlider.setSliderAdapter(adapter)
            report?.images?.forEach { adapter.addItem(it.image) }
        }

        if (viewModel.isCommunity()) {
            // HANDLE COMMUNITY

            if (report?.status?.equals("initial").orFalse()) {
                setupButton("Take Report") { handleTakeReport() }
            } else if (report?.status?.equals("onprogress").orFalse()) {
                setupButton("Finish Report") { finishReport() }
            } else if (report?.status?.equals("ondonation").orFalse()) {
                setupButton("Withdraw Donation") { handleWithdraw() }
                setupDonation()
            }
        } else {
            // HANDLE USER

            if (report?.status?.equals("ondonation").orFalse()) {
                setupButton("Donate Now") { handleDonation() }
                setupDonation()
            }
        }
    }

    private fun handleWithdraw() {
        goToActivity(WithdrawDonationActivity::class.java, Bundle().apply {
            putInt(WithdrawDonationActivity.EXTRA_ID, report?.id.orZero())
        })
    }

    private fun handleTakeReport() {
        goToActivity(TakeReportActivity::class.java, Bundle().apply {
            putParcelable(TakeReportActivity.EXTRA_DATA, report)
        })
    }

    private fun finishReport() {
        viewModel.finishReport(report?.id.orZero())
    }

    private fun handleDonation() {
        goToActivity(CreateDonationActivity::class.java, Bundle().apply {
            putInt(CreateDonationActivity.EXTRA_ID, report?.id.orZero())
        })
    }

    private fun setupDonation() {
        with(binding) {
            tvCurrentDonation.text = report?.currentDonation.toString()
            tvTotalDonation.text = "Accumulated from Rp ${report?.targetDonation.toString()}"
            progressDonation.progress = safeDivision(report?.currentDonation, report?.targetDonation)
            clDonation.visible()
        }
    }

    private fun setupButton(buttonText: String, action: () -> Unit) {
        binding.btnAction.apply {
            text = buttonText
            setOnClickListener { action.invoke() }
            visible()
        }
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}