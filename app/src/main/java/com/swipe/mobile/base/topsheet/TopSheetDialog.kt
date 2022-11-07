package com.swipe.mobile.base.topsheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.swipe.mobile.R

class TopSheetDialog : AppCompatDialog {
    constructor(context: Context) : super(context, getThemeResId(context, 0)) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    constructor(context: Context, @StyleRes theme: Int) : super(
        context,
        getThemeResId(context, theme)
    ) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    private constructor(
        context: Context, cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun setContentView(@LayoutRes layoutResId: Int) {
        super.setContentView(wrapInTopSheet(layoutResId, null, null))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun setContentView(view: View) {
        super.setContentView(wrapInTopSheet(0, view, null))
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(wrapInTopSheet(0, view, params))
    }

    private fun wrapInTopSheet(
        layoutResId: Int,
        view: View?,
        params: ViewGroup.LayoutParams?
    ): View {
        var view = view
        val coordinator = View.inflate(
            context,
            R.layout.layout_topsheet_dialog, null
        ) as CoordinatorLayout
        if (layoutResId != 0 && view == null) {
            view = layoutInflater.inflate(layoutResId, coordinator, false)
        }
        val topSheet = coordinator.findViewById<FrameLayout>(R.id.design_top_sheet)
        val topSheetBehavior = TopSheetBehavior.from(topSheet)
        topSheetBehavior.setTopSheetCallback(mTopSheetCallback)
        if (params == null) {
            topSheet.addView(view)
        } else {
            topSheet.addView(view, params)
        }

        shouldWindowCloseOnTouchOutside()
        coordinator.findViewById<View>(R.id.top_sheet_touch_outside)
            .setOnClickListener {
                if (isShowing) {
                    cancel()
                }
            }
        return coordinator
    }


    private fun shouldWindowCloseOnTouchOutside() {

        val value = TypedValue()
        context.theme
            .resolveAttribute(android.R.attr.windowCloseOnTouchOutside, value, true)
    }

    private val mTopSheetCallback: TopSheetBehavior.TopSheetCallback =
        object : TopSheetBehavior.TopSheetCallback() {
            override fun onStateChanged(
                topSheet: View,
                @BottomSheetBehavior.State newState: Int
            ) {
                if (newState == TopSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(topSheet: View, slideOffset: Float, isOpening: Boolean?) {
                if (slideOffset < 0.0000000001 && !isOpening!!) {
                    dismiss()

                }

            }
        }

    companion object {
        private const val TAG = "TopSheetDialog"
        private fun getThemeResId(context: Context, themeId: Int): Int {
            var id = themeId
            if (id == 0) {
                val outValue = TypedValue()
                id = if (context.theme.resolveAttribute(
                        com.google.android.material.R.attr.bottomSheetDialogTheme, outValue, true
                    )
                ) {
                    outValue.resourceId
                } else {
                    R.style.Theme_Design_TopSheetDialog
                }
            }
            return id
        }
    }
}