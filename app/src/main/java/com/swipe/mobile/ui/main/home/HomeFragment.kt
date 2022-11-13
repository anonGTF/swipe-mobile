package com.swipe.mobile.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.swipe.mobile.base.BaseFragment
import com.swipe.mobile.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }

}