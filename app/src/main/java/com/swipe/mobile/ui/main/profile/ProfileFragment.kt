package com.swipe.mobile.ui.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.swipe.mobile.base.BaseFragment
import com.swipe.mobile.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding =
        FragmentProfileBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }

}