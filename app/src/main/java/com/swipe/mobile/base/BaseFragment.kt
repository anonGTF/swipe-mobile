package com.swipe.mobile.base

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.swipe.mobile.base.dialog.BaseDialog
import com.swipe.mobile.base.dialog.BaseDialogInterface
import com.swipe.mobile.base.recyclerview.BaseRecyclerView
import com.swipe.mobile.utils.Resource
import com.swipe.mobile.utils.gone
import com.swipe.mobile.utils.visible

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    abstract fun setup()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showDialogLoading(dismiss: Boolean, message: String?) {
        val baseDialog = BaseDialog.BuildBaseDialog()
            .onBackPressedDismiss(dismiss)
            .setContent(message)
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog.show()
    }

    protected fun showDialogAlert(
        title: String?,
        message: String?,
        confirmCallback: () -> Unit?,
        drawableImage: Int?
    ) {
        val baseDialog = BaseDialog.BuildAlertDialog()
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
        baseDialog.show()
    }

    protected fun showDialogConfirmation(
        title: String?,
        message: String?,
        confirmCallback: () -> Unit?,
        cancelCallback: () -> Unit?,
        drawableImage: Int?
    ) {
        val baseDialog = BaseDialog.BuildConfirmationDialog()
            .onBackPressedDismiss(false)
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
        baseDialog.show()
    }

    protected fun showDialogPopImage(drawableImage: Int?) {
        val baseDialog = BaseDialog.BuildAlertDialog()
            .onBackPressedDismiss(false)
            .hideAllButton(true)
            .showPanelButton(true)
            .setImageContent(drawableImage)
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog.show()
    }

    private fun hideSoftKeyboard() {
        activity?.currentFocus?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(requireActivity(), InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected fun <T> setObserver(
        onSuccess: (Resource<T>) -> Unit,
        onError: (Resource<T>) -> Unit = {},
        onLoading: (Resource<T>) -> Unit = {},
        loadingItem: View? = null,
        swiperItem: SwipeRefreshLayout? = null
    ): Observer<in Resource<T>> {
        return Observer<Resource<T>> { data ->
            when(data) {
                is Resource.Success -> {
                    loadingItem?.gone()
                    swiperItem?.isRefreshing = false
                    onSuccess(data)
                }
                is Resource.Error -> {
                    loadingItem?.gone()
                    swiperItem?.isRefreshing = false
                    onError(data)
                }
                is Resource.Loading -> {
                    loadingItem?.visible()
                    onLoading(data)
                }
            }
        }
    }

    fun goToActivity(
        actDestination: Class<*>,
        data: Bundle? = null,
        clearIntent: Boolean = false,
        isFinish: Boolean = false
    ) {

        val intent = Intent(activity, actDestination)

        if (clearIntent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        data?.let { intent.putExtras(data) }

        startActivity(intent)

        if (isFinish) {
            activity?.finish()
        }
    }

    fun goToActivityForResult(resultCode: Int, actDestination: Class<*>, data: Bundle? = null) {
        val intent = Intent(activity, actDestination)
        data?.let { intent.putExtras(data) }
        startActivityForResult(intent, resultCode)
    }

    fun finishLoad(recycler: BaseRecyclerView?) {
        recycler?.let {
            it.getSwipeRefreshLayout().isRefreshing = false
            it.releaseBlock()
            it.stopShimmer()
        }
    }

    protected fun changeStatusBarColor(color: Int, iconDark: Boolean = false) {
        val window: Window = activity?.window!!
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor: View = requireActivity().window.decorView
            if (iconDark) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decor.systemUiVisibility = 0
            }

        }
    }

    protected fun setStatusBarTransparent() {
        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            statusBarColor = Color.TRANSPARENT
        }
    }

    protected fun showToast(message: String) =
        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
}