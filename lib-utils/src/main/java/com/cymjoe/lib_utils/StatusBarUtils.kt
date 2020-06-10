package com.cymjoe.lib_utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.IntDef
import com.readystatesoftware.systembartint.SystemBarTintManager

object StatusBarUtils {
    const val TYPE_MIUI = 0
    const val TYPE_FLYME = 1
    const val TYPE_M = 3//6.0

    @IntDef(TYPE_MIUI, TYPE_FLYME, TYPE_M)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class ViewType

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId 颜色
     */
    fun setStatusBarColor(activity: Activity, colorId: Int) {
        val window = activity.window
        window.statusBarColor = colorId
    }

    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    fun setTranslucentStatus(activity: Activity) {
        //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
        val window = activity.window
        val decorView = window.decorView
        //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        //导航栏颜色也可以正常设置
        //window.setNavigationBarColor(Color.TRANSPARENT);
    }


    /**
     * 代码实现android:fitsSystemWindows
     *
     * @param activity
     */
    @SuppressLint("ObsoleteSdkInt")
    fun setRootViewFitsSystemWindows(activity: Activity, fitSystemWindows: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winContent = activity.findViewById<View>(android.R.id.content) as ViewGroup
            if (winContent.childCount > 0) {
                val rootView = winContent.getChildAt(0) as ViewGroup
                rootView.fitsSystemWindows = fitSystemWindows
            }
        }

    }


    /**
     * 设置状态栏深色浅色切换
     */
    @SuppressLint("ObsoleteSdkInt")
    fun setStatusBarDarkTheme(activity: Activity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    setStatusBarFontIconDark(activity, TYPE_M, dark)
                }
                OSUtil.isMIUI -> {
                    setStatusBarFontIconDark(activity, TYPE_MIUI, dark)
                }
                OSUtil.isFlyme -> {
                    setStatusBarFontIconDark(activity, TYPE_FLYME, dark)
                }
                else -> {//其他情况
                    return false
                }
            }

            return true
        }
        return false
    }

    /**
     * 设置 状态栏深色浅色切换
     */
    private fun setStatusBarFontIconDark(
        activity: Activity,
        @ViewType type: Int,
        dark: Boolean
    ): Boolean {
        return setCommonUI(activity, dark)

    }

    //设置6.0 状态栏深色浅色切换
    private fun setCommonUI(activity: Activity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            var vis = decorView.systemUiVisibility
            vis = if (dark) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            if (decorView.systemUiVisibility != vis) {
                decorView.systemUiVisibility = vis
            }
            return true
        }
        return false
    }
    //获取状态栏高度
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}