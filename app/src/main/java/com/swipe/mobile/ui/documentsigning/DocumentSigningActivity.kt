package com.swipe.mobile.ui.documentsigning

import android.view.LayoutInflater
import com.squareup.picasso.Picasso
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.databinding.ActivityDocumentSigningBinding
import com.swipe.mobile.utils.gone
import com.swipe.mobile.utils.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DocumentSigningActivity : BaseActivity<ActivityDocumentSigningBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDocumentSigningBinding =
        ActivityDocumentSigningBinding::inflate

    override fun setup() {
        with(binding) {
            btnSign.setOnClickListener {
                cardSign.visible()
            }

            btnConfirm.setOnClickListener {
                cardSign.gone()
                btnSign.gone()

                val bitmap = signatureView.signatureBitmap
                imgSign.setImageBitmap(bitmap)
                imgSign.visible()
            }
        }
    }
}