package com.swipe.mobile.base.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.swipe.mobile.base.dialog.BaseDialog
import com.swipe.mobile.base.dialog.BaseDialogInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseBottomSheetDialogFragment<VB: ViewBinding> : BottomSheetDialogFragment(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    private var baseDialog: BaseDialog? = null
    private var dismissDialog: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun showDialogLoading(dismiss: Boolean, message: String?) {
        dismissDialog = dismiss
        baseDialog = BaseDialog.BuildBaseDialog()
            .onBackPressedDismiss(dismiss)
            .setContent(message)
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    fun showDialogAlert(
        title: String?,
        message: String?,
        confirmCallback: () -> Unit?,
        drawableImage: Int?
    ) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildAlertDialog()
            .onBackPressedDismiss(false)
            .setTitle(title)
            .setContent(message)
            .setSubmitButtonText("OK")
            .setImageContent(drawableImage)
            .setListener(object : BaseDialogInterface {
                override fun onSubmitClick() {
                    confirmCallback()
                }

                override fun onDismissClick() {

                }
            })
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()

    }

    fun showDialogConfirmation(
        title: String?,
        message: String?,
        confirmCallback: () -> Unit?,
        cancelCallback: () -> Unit?,
        drawableImage: Int?
    ) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildConfirmationDialog()
            .onBackPressedDismiss(dismissDialog)
            .setTitle(title)
            .setContent(message)
            .setImageContent(drawableImage)
            .setSubmitButtonText("OK")
            .setCancelButtonText("Cancel")
            .setSingleButton(false)
            .setListener(object : BaseDialogInterface {
                override fun onSubmitClick() {
                    confirmCallback()
                }

                override fun onDismissClick() {
                    cancelCallback()
                }
            })
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    fun showDialogPopImage(drawableImage: Int?) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildAlertDialog()
            .onBackPressedDismiss(dismissDialog)
            .hideAllButton(true)
            .showPanelButton(true)
            .setImageContent(drawableImage)
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    private fun hideSoftKeyboard() {
        activity?.currentFocus?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(requireActivity(), InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun hideLoading() {
        if (baseDialog != null) {
            if (baseDialog?.isShowing()!!) {
                baseDialog?.dismissDialog()
            }
        }
    }
}