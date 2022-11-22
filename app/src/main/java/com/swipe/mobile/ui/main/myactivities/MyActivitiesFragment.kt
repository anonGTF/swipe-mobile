package com.swipe.mobile.ui.main.myactivities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.swipe.mobile.base.BaseFragment
import com.swipe.mobile.data.model.BaseResponse
import com.swipe.mobile.data.model.Report
import com.swipe.mobile.databinding.FragmentMyActivitiesBinding
import com.swipe.mobile.ui.main.home.ReportAdapter
import com.swipe.mobile.ui.report.DetailReportActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyActivitiesFragment : BaseFragment<FragmentMyActivitiesBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMyActivitiesBinding =
        FragmentMyActivitiesBinding::inflate

    private val viewModel: MyActivitiesViewModel by activityViewModels()
    private val reportAdapter: ReportAdapter by lazy { ReportAdapter() }
    private val notificationAdapter: NotificationAdapter by lazy { NotificationAdapter() }

    override fun setup() {
        setupRecycler()

        notificationAdapter.differ.submitList(viewModel.getNotification())
        viewModel.getReports().observe(viewLifecycleOwner, handleReport())
    }

    private fun handleReport() = setObserver<BaseResponse<List<Report>>>(
        onSuccess = {
            reportAdapter.differ.submitList(it.data?.data)
        }
    )

    private fun setupRecycler() {
        with(binding.rvMyReport) {
            adapter = reportAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        reportAdapter.setOnItemClickListener {
            goToActivity(DetailReportActivity::class.java, Bundle().apply {
                putParcelable(DetailReportActivity.EXTRA_DATA, it)
            })
        }

        with(binding.rvNotification) {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}