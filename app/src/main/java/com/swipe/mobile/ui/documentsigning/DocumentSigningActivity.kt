package com.swipe.mobile.ui.documentsigning

import android.view.LayoutInflater
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityDocumentSigningBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentSigningActivity : BaseActivity<ActivityDocumentSigningBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDocumentSigningBinding =
        ActivityDocumentSigningBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}