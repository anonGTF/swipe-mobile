package com.swipe.mobile.ui.report

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.swipe.mobile.R
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.data.model.BaseResponse
import com.swipe.mobile.data.model.Report
import com.swipe.mobile.databinding.ActivityTakeReportBinding
import com.swipe.mobile.ui.main.MainActivity
import com.swipe.mobile.utils.orZero
import com.swipe.mobile.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TakeReportActivity : BaseActivity<ActivityTakeReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTakeReportBinding =
        ActivityTakeReportBinding::inflate

    private val viewModel: ReportViewModel by viewModels()
    private val adapter: ReportImageAdapter by lazy { ReportImageAdapter(this) }
    private var data: Report? = null
    private var isEnough = true

    override fun setup() {
        data = intent.getParcelableExtra(EXTRA_DATA)
        with(binding) {
            tvTitle.text = data?.title
            tvDesc.text = data?.desc
            tvDonation.text = data?.currentDonation.toString()
            chipCategory.text = data?.category

            imageSlider.setSliderAdapter(adapter)
            data?.images?.forEach { adapter.addItem(it.image) }

            rbIsEnough.setOnCheckedChangeListener { _, id ->
                isEnough = id == R.id.radio_yes

                if (!isEnough) {
                    binding.tvDonation.visible()
                    binding.tfDonation.visible()
                }
            }

            btnTakeReport.setOnClickListener {
                val donation =
                    if (isEnough) data?.currentDonation.orZero()
                    else etDonation.text.toString().toInt()

                viewModel.takeReport(
                    data?.id.orZero(),
                    etPic.text.toString(),
                    donation
                ).observe(this@TakeReportActivity, handleReport())
            }
        }
    }

    private fun handleReport() = setObserver<BaseResponse<Report>>(
        onSuccess = {
            showToast("Report has been taken")
            goToActivity(MainActivity::class.java, null, clearIntent = true, isFinish = true)
        }
    )

    companion object {
        const val EXTRA_DATA = "data"
    }
}