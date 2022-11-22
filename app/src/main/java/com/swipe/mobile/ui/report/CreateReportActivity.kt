package com.swipe.mobile.ui.report

import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.swipe.mobile.R
import com.swipe.mobile.base.BaseActivity
import com.swipe.mobile.data.model.BaseResponse
import com.swipe.mobile.data.model.Report
import com.swipe.mobile.databinding.ActivityCreateReportBinding
import com.swipe.mobile.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateReportActivity : BaseActivity<ActivityCreateReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCreateReportBinding =
        ActivityCreateReportBinding::inflate

    private val viewModel: ReportViewModel by viewModels()
    private var category: String = ""

    override fun setup() {
        setupCategory()
        with(binding) {
            inputCategory.setOnItemClickListener { _, _, position, _ ->
                val adapter = binding.inputCategory.adapter
                category = adapter.getItem(position) as String
            }
            btnCreateReport.setOnClickListener {
                val title = etTitle.text.toString()
                val desc = etDesc.text.toString()
                val location = etLocation.text.toString()
                viewModel.createReport(title, desc, category, location).observe(this@CreateReportActivity, handleReport())
            }
        }
    }

    private fun handleReport() = setObserver<BaseResponse<Report>>(
        onSuccess = {
            showToast("Report has been created")
            goToActivity(MainActivity::class.java, null, clearIntent = true, isFinish = true)
        },
        onError = { showToast(it.message.toString()) }
    )

    private fun setupCategory() {
        val adapter = ArrayAdapter(
            this,
            R.layout.item_category,
            ArrayList(viewModel.getCategories())
        )
        binding.inputCategory.setAdapter(adapter)
    }
}