package com.swipe.mobile.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.swipe.mobile.base.BaseAdapter
import com.swipe.mobile.data.model.Report
import com.swipe.mobile.databinding.ItemReportBinding
import com.swipe.mobile.utils.*

class ReportAdapter: BaseAdapter<ItemReportBinding, Report>() {
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemReportBinding =
        ItemReportBinding::inflate

    override fun bind(binding: ItemReportBinding, item: Report) {
        with(binding) {
            tvTitle.text = item.title
            tvUsername.text = "galih"
            if (item.status == "ondonation") {
                tvCurrentDonation.text = item.currentDonation.toString()
                tvDaysLeft.text = item.dueDate
                progressDonation.progress = safeDivision(item.currentDonation, item.targetDonation)
            }
            if (item.images?.isNotEmpty().orFalse()) {
                Picasso.get().load(BASE_IMAGE_URL).into(img)
            } else {
                Picasso.get().load(BASE_IMAGE_URL).into(img)
            }
        }
    }
}