package com.swipe.mobile.ui.main.homeuser

import android.view.LayoutInflater
import android.view.ViewGroup
import com.swipe.mobile.base.BaseFragment
import com.swipe.mobile.databinding.FragmentHomeUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeUserFragment : BaseFragment<FragmentHomeUserBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeUserBinding =
        FragmentHomeUserBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }

}