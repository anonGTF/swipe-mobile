package com.swipe.mobile.base.topsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseTopSheetFragment<VB : ViewBinding> : AppCompatDialogFragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dial = TopSheetDialog(
            requireContext(),
            theme
        )
        dial.setOnShowListener { dialog ->
            val d = dialog as TopSheetDialog
            val topSheet = requireView().parent as View
            TopSheetBehavior.from(topSheet).setState(TopSheetBehavior.STATE_EXPANDED)
            topSheet.minimumHeight = 0

        }
        return dial
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}