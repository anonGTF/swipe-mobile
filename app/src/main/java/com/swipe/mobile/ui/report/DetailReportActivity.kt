package com.swipe.mobile.ui.report

import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityDetailReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReportActivity : BaseActivity<ActivityDetailReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDetailReportBinding =
        ActivityDetailReportBinding::inflate

    override fun setup() {
    }
}