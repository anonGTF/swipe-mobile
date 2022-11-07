package com.swipe.mobile.ui.report

import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityCreateReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateReportActivity : BaseActivity<ActivityCreateReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCreateReportBinding =
        ActivityCreateReportBinding::inflate

    override fun setup() {
    }
}