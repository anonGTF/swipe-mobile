package com.swipe.mobile.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.swipe.mobile.base.BaseFragment
import com.swipe.mobile.data.model.BaseResponse
import com.swipe.mobile.data.model.Report
import com.swipe.mobile.databinding.FragmentHomeBinding
import com.swipe.mobile.ui.report.CreateReportActivity
import com.swipe.mobile.ui.report.DetailReportActivity
import com.swipe.mobile.utils.gone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    private val reportAdapter: ReportAdapter by lazy { ReportAdapter() }
    private val viewModel: HomeViewModel by activityViewModels()

    override fun setup() {
        setupRecycler()

        viewModel.getReports(1).observe(viewLifecycleOwner, handleReport())

        binding.btnCreateReport.setOnClickListener {
            goToActivity(CreateReportActivity::class.java)
        }

        if (viewModel.isCommunity()) {
            binding.cvTop.gone()
        }
    }

    private fun handleReport() = setObserver<BaseResponse<List<Report>>>(
        onSuccess = { data ->
            reportAdapter.differ.submitList(data.data?.data)
        },
        onError = { showToast(it.message.toString()) },
        loadingItem = binding.progressBar
    )

    private fun setupRecycler() {
        with(binding.rvReport) {
            adapter = reportAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        reportAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable(DetailReportActivity.EXTRA_DATA, it)
            }
            goToActivity(DetailReportActivity::class.java, bundle)
        }
    }

}