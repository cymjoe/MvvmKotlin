# MvvmKotlin
Mvvm Kotlin 模块化 测试库  集成ARouter  kotlin协程 Retrofit  EventBus  Databinding（package com.cymjoe.lib_widget.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.cymjoe.baselibrary.utils.log

class HeadRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    //手指抬起Y高度
    var lastY = 0f

    //阻尼
    var damp = 0.6f

    //展开系数
    var status = 0.5f

    //按下y坐标
    private var touchY = 0.0f

    //动画
    private lateinit var animator: ValueAnimator

    //拖动距离
    var n = 0

    //是否在顶部
    var isTop = false

    //是否打开
    var isOpen = false


    //二级高度
    private var heights = 0

    //滚动监听
    private var onScrollListener: OnScrollListener? = null
    private var canScroll: CanScrollListener? = null


    fun setHeights(heights: Int) {
        this.heights = heights
    }

    public fun setOnScrollListener(onScrollListener: OnScrollListener) {
        this.onScrollListener = onScrollListener
    }

    public fun setCanScrollListener(canScroll: CanScrollListener) {
        this.canScroll = canScroll
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        isTop = !canScrollVertically(-1)
    }

    /**
     * 滚动监听
     */
    interface OnScrollListener {
        fun scroll(y: Int)

    }

    /**
     * 是否可以滑动
     */
    interface CanScrollListener {
        fun canScroll(y: Boolean)

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                touchY = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> {

                n = (((ev.rawY - touchY) * damp).toInt() + lastY).toInt()

                if (isTop) {

                    if (n >= heights) {
                        canScroll?.canScroll(false)
                        return false
                    }
                    if (n > 0) {
                        canScroll?.canScroll(false)
                        top = n
                        onScrollListener?.scroll(n)
                        return true
                    }
                }

            }
            MotionEvent.ACTION_UP -> {
                if (isOpen) {
                    if (n < lastY) {
                        takeAnim(n, 0)
                        n = 0
                        lastY = 0f
                        return true
                    }
                } else {
                    lastY = n.toFloat()
                    if (isTop) {
                        if (n > heights * status) {
                            if (n > heights) {
                                n = heights
                            }
                            takeAnim(n, heights)
                            lastY = heights.toFloat()
                            return true
                        } else {
                            takeAnim(n, 0)
                            n = 0
                            lastY = 0f
                        }
                    } else {
                        lastY = 0f
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun takeAnim(start: Int, end: Int) {
        animator = ValueAnimator.ofInt(start, end)
        animator.duration = 300
        animator.start()
        animator.addUpdateListener {
            top = (it.animatedValue as Int)
            onScrollListener?.scroll(top)
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (end == 0) {
                    isOpen = false
                    canScroll?.canScroll(true)
                } else if (end==heights){
                    isOpen = true
                }
            }
        })
    }
}
