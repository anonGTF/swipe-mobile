package com.swipe.mobile.base.topsheet

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.ClassLoaderCreator
import android.util.AttributeSet
import android.view.*
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.google.android.material.R
import java.lang.ref.WeakReference
import kotlin.math.abs
import kotlin.math.max

open class TopSheetBehavior<V : View>(context: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    abstract class TopSheetCallback {
        abstract fun onStateChanged(topSheet: View, @State newState: Int)
        abstract fun onSlide(topSheet: View, slideOffset: Float, isOpening: Boolean?)
    }


    @IntDef(STATE_EXPANDED, STATE_COLLAPSED, STATE_DRAGGING, STATE_SETTLING, STATE_HIDDEN)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class State

    private var mMaximumVelocity = 0f
    private var mPeekHeight = 0
    private var mMinOffset = 0
    private var mMaxOffset = 0
    private var mHideable = false
    private var mSkipCollapsed = false

    @State
    private var mState = STATE_COLLAPSED
    private var mViewDragHelper: ViewDragHelper? = null
    private var mIgnoreEvents = false
    private var mLastNestedScrollDy = 0
    private var mNestedScrolled = false
    private var mViewRef: WeakReference<V?>? = null
    private var mNestedScrollingChildRef: WeakReference<View?>? = null
    private var mCallback: TopSheetCallback? = null
    private var mVelocityTracker: VelocityTracker? = null
    private var mActivePointerId = 0
    private var mInitialY = 0
    private var mTouchingScrollingChild = false

    override fun onSaveInstanceState(parent: CoordinatorLayout, child: V): Parcelable? {
        return SavedState(super.onSaveInstanceState(parent, child), mState)
    }

    override fun onRestoreInstanceState(parent: CoordinatorLayout, child: V, state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(parent, child, ss.superState)
        // Intermediate states are restored as collapsed state
        mState = if (ss.state == STATE_DRAGGING || ss.state == STATE_SETTLING) {
            STATE_COLLAPSED
        } else {
            ss.state
        }
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) {
            child.fitsSystemWindows = true
            //            ViewCompat.setFitsSystemWindows(child, true);
        }
        val savedTop = child.top
        // First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection)
        // Offset the bottom sheet
        val mParentHeight = parent.height
        mMinOffset = max(-child.height, -(child.height - mPeekHeight))
        mMaxOffset = 0
        if (mState == STATE_EXPANDED) {
            ViewCompat.offsetTopAndBottom(child, mMaxOffset)
        } else if (mHideable && mState == STATE_HIDDEN) {
            ViewCompat.offsetTopAndBottom(child, -child.height)
        } else if (mState == STATE_COLLAPSED) {
            ViewCompat.offsetTopAndBottom(child, mMinOffset)
        } else if (mState == STATE_DRAGGING || mState == STATE_SETTLING) {
            ViewCompat.offsetTopAndBottom(child, savedTop - child.top)
        }
        if (mViewDragHelper == null) {
            mViewDragHelper = ViewDragHelper.create(parent, mDragCallback)
        }
        mViewRef = WeakReference(child)
        mNestedScrollingChildRef = WeakReference(findScrollingChild(child))
        return true
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        if (!child!!.isShown) {
            return false
        }
        val action = event.actionMasked
        // Record the velocity
        if (action == MotionEvent.ACTION_DOWN) {
            reset()
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mTouchingScrollingChild = false
                mActivePointerId = MotionEvent.INVALID_POINTER_ID
                // Reset the ignore flag
                if (mIgnoreEvents) {
                    mIgnoreEvents = false
                    return false
                }
            }
            MotionEvent.ACTION_DOWN -> {
                val initialX = event.x.toInt()
                mInitialY = event.y.toInt()
                val scroll = mNestedScrollingChildRef!!.get()
                if (scroll != null && parent.isPointInChildBounds(scroll, initialX, mInitialY)) {
                    mActivePointerId = event.getPointerId(event.actionIndex)
                    mTouchingScrollingChild = true
                }
                mIgnoreEvents = mActivePointerId == MotionEvent.INVALID_POINTER_ID &&
                        !parent.isPointInChildBounds(child, initialX, mInitialY)
            }
        }
        if (!mIgnoreEvents && mViewDragHelper!!.shouldInterceptTouchEvent(event)) {
            return true
        }
        // We have to handle cases that the ViewDragHelper does not capture the bottom sheet because
        // it is not the top most view of its parent. This is not necessary when the touch event is
        // happening over the scrolling content as nested scrolling logic handles that case.
        val scroll = mNestedScrollingChildRef!!.get()
        return action == MotionEvent.ACTION_MOVE && scroll != null &&
                !mIgnoreEvents && mState != STATE_DRAGGING &&
                !parent.isPointInChildBounds(scroll, event.x.toInt(), event.y.toInt()) && abs(
            mInitialY - event.y
        ) > mViewDragHelper!!.touchSlop
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        if (!child.isShown) {
            return false
        }
        val action = event.actionMasked
        if (mState == STATE_DRAGGING && action == MotionEvent.ACTION_DOWN) {
            return true
        }
        if (mViewDragHelper != null) {
            //no crash
            mViewDragHelper!!.processTouchEvent(event)
            // Record the velocity
            if (action == MotionEvent.ACTION_DOWN) {
                reset()
            }
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain()
            }
            mVelocityTracker!!.addMovement(event)
            // The ViewDragHelper tries to capture only the top-most View. We have to explicitly tell it
            // to capture the bottom sheet in case it is not captured and the touch slop is passed.
            if (action == MotionEvent.ACTION_MOVE && !mIgnoreEvents) {
                if (abs(mInitialY - event.y) > mViewDragHelper!!.touchSlop) {
                    mViewDragHelper!!.captureChildView(child, event.getPointerId(event.actionIndex))
                }
            }
        }
        return !mIgnoreEvents
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        mLastNestedScrollDy = 0
        mNestedScrolled = false
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        val scrollingChild = mNestedScrollingChildRef!!.get()
        if (target !== scrollingChild) {
            return
        }
        val currentTop = child.top
        val newTop = currentTop - dy
        if (dy > 0) { // Upward
            if (!target.canScrollVertically(1)) {
                if (newTop >= mMinOffset || mHideable) {
                    consumed[1] = dy
                    ViewCompat.offsetTopAndBottom(child, -dy)
                    setStateInternal(STATE_DRAGGING)
                } else {
                    consumed[1] = currentTop - mMinOffset
                    ViewCompat.offsetTopAndBottom(child, -consumed[1])
                    setStateInternal(STATE_COLLAPSED)
                }
            }
        } else if (dy < 0) { // Downward
            // Negative to check scrolling up, positive to check scrolling down
            if (newTop < mMaxOffset) {
                consumed[1] = dy
                ViewCompat.offsetTopAndBottom(child, -dy)
                setStateInternal(STATE_DRAGGING)
            } else {
                consumed[1] = currentTop - mMaxOffset
                ViewCompat.offsetTopAndBottom(child, -consumed[1])
                setStateInternal(STATE_EXPANDED)
            }
        }
        dispatchOnSlide(child.top)
        mLastNestedScrollDy = dy
        mNestedScrolled = true
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        if (child.top == mMaxOffset) {
            setStateInternal(STATE_EXPANDED)
            return
        }
        if (target !== mNestedScrollingChildRef!!.get() || !mNestedScrolled) {
            return
        }
        val top: Int
        val targetState: Int
        if (mLastNestedScrollDy < 0) {
            top = mMaxOffset
            targetState = STATE_EXPANDED
        } else if (mHideable && shouldHide(child, getYVelocity())) {
            top = -child.height
            targetState = STATE_HIDDEN
        } else if (mLastNestedScrollDy == 0) {
            val currentTop = child.top
            if (abs(currentTop - mMinOffset) > abs(currentTop - mMaxOffset)) {
                top = mMaxOffset
                targetState = STATE_EXPANDED
            } else {
                top = mMinOffset
                targetState = STATE_COLLAPSED
            }
        } else {
            top = mMinOffset
            targetState = STATE_COLLAPSED
        }
        if (mViewDragHelper!!.smoothSlideViewTo(child, child.left, top)) {
            setStateInternal(STATE_SETTLING)
            ViewCompat.postOnAnimation(child, SettleRunnable(child, targetState))
        } else {
            setStateInternal(targetState)
        }
        mNestedScrolled = false
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout, child: V, target: View,
        velocityX: Float, velocityY: Float
    ): Boolean {
        return target === mNestedScrollingChildRef!!.get() &&
                (mState != STATE_EXPANDED ||
                        super.onNestedPreFling(
                            coordinatorLayout, child, target,
                            velocityX, velocityY
                        ))
    }

    private var peekHeight: Int = 0
        get() = mPeekHeight

    fun setHideable(hideable: Boolean) {
        mHideable = hideable
    }


    fun isHideable(): Boolean {
        return mHideable
    }


    fun setSkipCollapsed(skipCollapsed: Boolean) {
        mSkipCollapsed = skipCollapsed
    }

    fun getSkipCollapsed(): Boolean {
        return mSkipCollapsed
    }

    fun setTopSheetCallback(callback: TopSheetCallback?) {
        mCallback = callback
    }


    fun setState(@State state: Int) {
        if (state == mState) {
            return
        }
        if (mViewRef == null) {
            // The view is not laid out yet; modify mState and let onLayoutChild handle it later
            if (state == STATE_COLLAPSED || state == STATE_EXPANDED ||
                mHideable && state == STATE_HIDDEN
            ) {
                mState = state
            }
            return
        }
        val child = mViewRef!!.get() ?: return
        val top: Int
        top = if (state == STATE_COLLAPSED) {
            mMinOffset
        } else if (state == STATE_EXPANDED) {
            mMaxOffset
        } else if (mHideable && state == STATE_HIDDEN) {
            -child.height
        } else {
            throw IllegalArgumentException("Illegal state argument: $state")
        }
        setStateInternal(STATE_SETTLING)
        if (mViewDragHelper!!.smoothSlideViewTo(child, child.left, top)) {
            ViewCompat.postOnAnimation(child, SettleRunnable(child, state))
        }
    }


    @State
    fun getState(): Int {
        return mState
    }

    var oldState = mState
    private fun setStateInternal(@State state: Int) {
        if (state == STATE_COLLAPSED || state == STATE_EXPANDED) {
            oldState = state
        }
        if (mState == state) {
            return
        }
        mState = state
        val bottomSheet: View? = mViewRef!!.get()
        if (bottomSheet != null && mCallback != null) {
            mCallback!!.onStateChanged(bottomSheet, state)
        }
    }

    private fun reset() {
        mActivePointerId = ViewDragHelper.INVALID_POINTER
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    private fun shouldHide(child: View, yvel: Float): Boolean {
        if (child.top > mMinOffset) {
            // It should not hide, but collapse.
            return false
        }
        val newTop = child.top + yvel * HIDE_FRICTION
        return abs(newTop - mMinOffset) / mPeekHeight.toFloat() > HIDE_THRESHOLD
    }

    private fun findScrollingChild(view: View): View? {
        if (view is NestedScrollingChild) {
            return view
        }
        if (view is ViewGroup) {
            var i = 0
            val count = view.childCount
            while (i < count) {
                val scrollingChild = findScrollingChild(view.getChildAt(i))
                if (scrollingChild != null) {
                    return scrollingChild
                }
                i++
            }
        }
        return null
    }

    private fun getYVelocity(): Float {
        mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity)
        return mVelocityTracker!!.getYVelocity(mActivePointerId)
    }

    private val mDragCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (mState == STATE_DRAGGING) {
                return false
            }
            if (mTouchingScrollingChild) {
                return false
            }
            if (mState == STATE_EXPANDED && mActivePointerId == pointerId) {
                val scroll = mNestedScrollingChildRef!!.get()
                if (scroll != null && scroll.canScrollVertically(-1)) {
                    return false
                }
            }
            return mViewRef != null && mViewRef!!.get() === child
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            dispatchOnSlide(top)
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state == ViewDragHelper.STATE_DRAGGING) {
                setStateInternal(STATE_DRAGGING)
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val top: Int
            @State val targetState: Int
            if (yvel > 0) { // Moving up
                top = mMaxOffset
                targetState = STATE_EXPANDED
            } else if (mHideable && shouldHide(releasedChild, yvel)) {
                top = -mViewRef!!.get()!!.height
                targetState = STATE_HIDDEN
            } else if (yvel == 0f) {
                val currentTop = releasedChild.top
                if (abs(currentTop - mMinOffset) > abs(currentTop - mMaxOffset)) {
                    top = mMaxOffset
                    targetState = STATE_EXPANDED
                } else {
                    top = mMinOffset
                    targetState = STATE_COLLAPSED
                }
            } else {
                top = mMinOffset
                targetState = STATE_COLLAPSED
            }
            if (mViewDragHelper!!.settleCapturedViewAt(releasedChild.left, top)) {
                setStateInternal(STATE_SETTLING)
                ViewCompat.postOnAnimation(
                    releasedChild,
                    SettleRunnable(releasedChild, targetState)
                )
            } else {
                setStateInternal(targetState)
            }
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return constrain(top, if (mHideable) -child.height else mMinOffset, mMaxOffset)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return child.left
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return if (mHideable) {
                child.height
            } else {
                mMaxOffset - mMinOffset
            }
        }
    }

    private fun dispatchOnSlide(top: Int) {
        val bottomSheet: View? = mViewRef!!.get()
        if (bottomSheet != null && mCallback != null) {
            val isOpening = oldState == STATE_COLLAPSED
            if (top < mMinOffset) {
                mCallback!!.onSlide(
                    bottomSheet,
                    (top - mMinOffset).toFloat() / mPeekHeight,
                    isOpening
                )
            } else {
                mCallback!!.onSlide(
                    bottomSheet,
                    (top - mMinOffset).toFloat() / (mMaxOffset - mMinOffset), isOpening
                )
            }
        }
    }

    private inner class SettleRunnable internal constructor(
        private val mView: View,
        @field:State @param:State private val mTargetState: Int
    ) :
        Runnable {
        override fun run() {
            if (mViewDragHelper != null && mViewDragHelper!!.continueSettling(true)) {
                ViewCompat.postOnAnimation(mView, this)
            } else {
                setStateInternal(mTargetState)
            }
        }
    }

    protected class SavedState : AbsSavedState {
        @State
        val state: Int

        @RequiresApi(api = Build.VERSION_CODES.N)
        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            state = source.readInt()
        }

        constructor(superState: Parcelable?, @State state: Int) : super(superState) {
            this.state = state
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(state)
        }

        companion object {
            @JvmField
            val CREATOR: ClassLoaderCreator<SavedState> = object : ClassLoaderCreator<SavedState> {
                @RequiresApi(api = Build.VERSION_CODES.N)
                override fun createFromParcel(source: Parcel): SavedState {
                    return createFromParcel(source, null)
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                override fun createFromParcel(source: Parcel, loader: ClassLoader?): SavedState {
                    return SavedState(source, loader)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        const val STATE_DRAGGING = 1
        const val STATE_SETTLING = 2
        const val STATE_EXPANDED = 3
        const val STATE_COLLAPSED = 4
        const val STATE_HIDDEN = 5
        private const val HIDE_THRESHOLD = 0.5f
        private const val HIDE_FRICTION = 0.1f

        fun <V : View> from(view: V): TopSheetBehavior<V> {
            val params = view!!.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior = params
                .behavior
            require(behavior is TopSheetBehavior<*>) { "The view is not associated with TopSheetBehavior" }
            return behavior as TopSheetBehavior<V>
        }

        fun constrain(amount: Int, low: Int, high: Int): Int {
            return if (amount < low) low else amount.coerceAtMost(high)
        }

        fun constrain(amount: Float, low: Float, high: Float): Float {
            return if (amount < low) low else amount.coerceAtMost(high)
        }
    }

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.BottomSheetBehavior_Layout
        )
        peekHeight = a.getDimensionPixelSize(
            R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, 0
        )
        setHideable(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false))
        setSkipCollapsed(
            a.getBoolean(
                R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed,
                false
            )
        )
        a.recycle()
        val configuration = ViewConfiguration.get(context)
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity.toFloat()
    }
}