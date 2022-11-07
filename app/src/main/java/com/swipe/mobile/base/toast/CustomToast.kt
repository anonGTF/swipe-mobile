package com.swipe.mobile.base.toast

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.swipe.mobile.R


class CustomToast private constructor(
    private val context: Context,
    private var message: CharSequence,
    private var duration: Int
) {

    private var rootView: View? = null
    private var leftDrawableRes: Int? = null
    private var backgroundColor: Int? = null
    private var rightDrawableRes: Int? = null

    companion object {
        const val LENGTH_SHORT = Toast.LENGTH_SHORT
        const val LENGTH_LONG = Toast.LENGTH_LONG

        fun pop(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toast {
            return pop(prepare(context, message, duration))
        }

        fun pop(
            context: Context,
            message: CharSequence,
            drawableRes: Int,
            duration: Int
        ): Toast {
            return pop(prepare(context, message, drawableRes, duration))
        }

        fun pop(toaster: CustomToast): Toast {
            return Toast(toaster.context).apply {
                setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0,
                    toaster.context.resources.getDimensionPixelOffset(com.intuit.sdp.R.dimen._60sdp))
                duration = toaster.duration
                view = toaster.rootView
            }
        }

        fun popSuccess(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toast {
            return pop(prepareSuccess(context, message, duration))
        }

        fun popWarning(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toast {
            return pop(prepareWarning(context, message, duration))
        }

        fun popError(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toast {
            return pop(prepareError(context, message, duration))
        }

        private fun prepare(context: Context, message: CharSequence, duration: Int): CustomToast {
            return prepare(context, message, null, duration)
        }

        private fun prepare(
            context: Context,
            message: CharSequence,
            drawableRes: Int?,
            duration: Int
        ): CustomToast {
            return Builder(context)
                .setMessage(message)
                .apply {
                    drawableRes?.let {
                        setLeftDrawable(it)
                    }
                }
                .setDuration(duration)
                .make()
        }

        private fun prepareSuccess(
            context: Context,
            message: CharSequence,
            duration: Int
        ): CustomToast {
            return Builder(context)
                .setMessage(message)
                .setRightDrawable(R.drawable.ic_round_close)
                .setBackground(R.color.turquoise_secondary_2)
                .setDuration(duration)
                .make()
        }

        private fun prepareWarning(
            context: Context,
            message: CharSequence,
            duration: Int
        ): CustomToast {
            return Builder(context)
                .setMessage(message)
                .setDuration(duration)
                .make()
        }

        private fun prepareError(context: Context, message: CharSequence, duration: Int): CustomToast {
            return Builder(context)
                .setMessage(message)
                .setRightDrawable(R.drawable.ic_round_close)
                .setBackground(R.color.red_shades_90)
                .setDuration(duration)
                .make()
        }

    }

    class Builder(private val context: Context) {

        private val rootView: View
        private var messageTextView: TextView? = null
        private var rightDrawableImageView: AppCompatImageView? = null
        private var backCardView: ConstraintLayout? = null
        private var leftDrawableRes: Int? = null
        private var rightDrawableRes: Int? = null
        private var backgroundColor: Int? = null
        private var colorStripView: View? = null

        private var message: CharSequence = ""
        private var duration: Int = LENGTH_SHORT

        init {
            rootView = initView(context)
        }

        private fun initView(context: Context): View {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val rootView = inflater.inflate(R.layout.layout_toast_base, RelativeLayout(context), false)

            messageTextView = rootView.findViewById(R.id.tvMessage)
            val typeface = ResourcesCompat.getFont(context, R.font.baloo2_bold)
            messageTextView!!.setTypeface(typeface)
            rightDrawableImageView = rootView.findViewById(R.id.ivRightDrawable)
            rightDrawableImageView = rootView.findViewById(R.id.ivRightDrawable)
            backCardView = rootView.findViewById(R.id.clToast)

            setInitViewProperties()

            return rootView
        }

        fun setMessage(message: CharSequence) = apply {
            this.message = message
            messageTextView?.text = message
            messageTextView?.visibility = View.VISIBLE
        }

        fun setLeftDrawable(leftDrawableRes: Int) = apply {
            this.leftDrawableRes = leftDrawableRes
        }

        fun setBackground(@ColorRes backgroundColor: Int) = apply {
            backCardView?.background = ContextCompat.getDrawable(context, backgroundColor)
        }

        fun setRightDrawable(rightDrawableRes: Int) = apply {
            this.rightDrawableRes = rightDrawableRes
            rightDrawableImageView?.setImageResource(rightDrawableRes)
            rightDrawableImageView?.visibility = View.VISIBLE
        }

        fun setDuration(duration: Int) = apply {
            this.duration = duration
        }

        fun setRightDrawableTint(colorRes: Int) = apply {
            rightDrawableImageView?.setColorFilter(ContextCompat.getColor(context, colorRes))
            rightDrawableImageView?.visibility = View.VISIBLE
        }

        fun setStripTint(colorRes: Int) = apply {
            colorStripView?.setBackgroundColor(ContextCompat.getColor(this.context, colorRes))
            colorStripView?.visibility = View.VISIBLE
        }

        fun make(): CustomToast {
            return CustomToast(context, message, duration).also {
                it.rootView = rootView
                it.leftDrawableRes = leftDrawableRes
            }
        }

        private fun setInitViewProperties() {
            colorStripView?.visibility = View.GONE
            messageTextView?.visibility = View.GONE
        }

    }

}