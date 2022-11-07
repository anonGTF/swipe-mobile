package com.swipe.mobile.ui.report

import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityTakeReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TakeReportActivity : BaseActivity<ActivityTakeReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTakeReportBinding =
        ActivityTakeReportBinding::inflate

    override fun setup() {
    }
}