package com.swipe.mobile.ui.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import com.swipe.mobile.base.BaseFragment
import com.swipe.mobile.data.model.BaseResponse
import com.swipe.mobile.data.model.User
import com.swipe.mobile.databinding.FragmentProfileBinding
import com.swipe.mobile.ui.auth.LandingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding =
        FragmentProfileBinding::inflate

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun setup() {
        viewModel.getProfile().observe(viewLifecycleOwner, handleProfile())

        binding.btnLogout.setOnClickListener { viewModel.logout().observe(viewLifecycleOwner, handleLogout()) }
    }

    private fun handleProfile() = setObserver<BaseResponse<User>>(
        onSuccess = {
            with(binding) {
                tvName.text = it.data?.data?.name
                Picasso.get().load(it.data?.data?.avatar).into(imgAvatar)
            }
        },
        onError = { showToast(it.message.toString()) },
        loadingItem = binding.progressBar
    )

    private fun handleLogout() = setObserver<BaseResponse<String>>(
        onSuccess = { goToActivity(LandingActivity::class.java, null,
            clearIntent = true,
            isFinish = true
        ) }
    )
}