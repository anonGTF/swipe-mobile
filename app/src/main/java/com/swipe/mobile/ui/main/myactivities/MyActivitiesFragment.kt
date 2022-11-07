package com.swipe.mobile.ui.main.myactivities

import android.view.LayoutInflater
import android.view.ViewGroup
import com.swipe.mobile.base.BaseFragment
import com.swipe.mobile.databinding.FragmentMyActivitiesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyActivitiesFragment : BaseFragment<FragmentMyActivitiesBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMyActivitiesBinding =
        FragmentMyActivitiesBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }

}