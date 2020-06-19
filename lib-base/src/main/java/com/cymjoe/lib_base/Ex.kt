package com.cymjoe.lib_base

import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_aroute.ARouteUtils
import com.cymjoe.lib_base.constant.Constant
import com.cymjoe.lib_http.States
import com.cymjoe.lib_utils.LogUtil
import com.cymjoe.lib_utils.RSAUtils
import com.tencent.mmkv.MMKV
import org.greenrobot.eventbus.EventBus
import java.util.*


fun Any.launch(clazz: String) {
    ARouteUtils.launch(clazz)
}


fun Activity.launchOver(clazz: String) {
    launch(clazz)
    finish()
}


fun Any.toLogin() {
    launch(ARoutePath.LoginActivity)
}

fun Activity.finishActivity(activityName: String) {
    EventBus.getDefault().post(activityName)
}

fun Any.toast(msg: String) {
    if (msg.isNoEmpty()) {
        if (msg == States.error) {
            ToastUtils.showShort(R.string.string_error)
        } else {
            ToastUtils.showShort(msg)
        }
    }
}

// RSA加密
fun Any.encrypt(msg: String): String {
    return RSAUtils.encrypt(msg, RSAUtils.getPublicKey(Constant.PUBLIC_KEY))
}

//判断字符串非空
fun String.isNoEmpty(): Boolean {
    return this.isNotEmpty() || this != "null" || this != "NULL"
}

//本地存储
fun Any.mmkv(): MMKV {
    return MMKV.defaultMMKV()
}
