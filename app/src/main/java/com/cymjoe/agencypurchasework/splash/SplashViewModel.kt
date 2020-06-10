package com.cymjoe.agencypurchasework.splash

import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.PermissionUtils
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_base.launchOver
import com.cymjoe.lib_http.BaseViewModel

class SplashViewModel : BaseViewModel() {
    var hasPermission = MutableLiveData<Boolean>()

    //获取权限
    @SuppressLint("WrongConstant")
    fun getPermission() {
        PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    hasPermission.value = true
                }

                override fun onDenied() {
                    hasPermission.value = false
                }
            }).request()

    }

}