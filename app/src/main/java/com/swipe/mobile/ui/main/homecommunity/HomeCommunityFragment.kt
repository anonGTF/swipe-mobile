package com.swipe.mobile.ui.main.homecommunity

import android.view.LayoutInflater
import android.view.ViewGroup
import com.swipe.mobile.base.BaseFragment
import com.swipe.mobile.databinding.FragmentHomeCommunityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeCommunityFragment : BaseFragment<FragmentHomeCommunityBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeCommunityBinding =
        FragmentHomeCommunityBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}