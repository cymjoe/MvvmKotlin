package com.cymjoe.lib_aroute

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter

object ARouteUtils {
    fun launch(activityName: String?) {
        ARouter.getInstance().build(activityName)
            .navigation()
    }

}