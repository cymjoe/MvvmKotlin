package com.cymjoe.moudle_login.login

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cymjoe.lib_base.utils.NetUtils
import com.cymjoe.lib_http.BaseViewModel
import com.cymjoe.lib_http.dataConvert
import com.cymjoe.lib_module.LoginRequestEntity
import com.cymjoe.moudle_login.LoginService


class LoginViewModel : BaseViewModel() {

    private val api = NetUtils.getService(LoginService::class.java)

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    val userName: LiveData<String> = username

    fun onClick(view: View) {
        val userName = username.value
        if (TextUtils.isEmpty(userName)) {
            emitUiState(errorMsg = "请输入用户名")
            return
        }
        val password = password.value
        if (TextUtils.isEmpty(password)) {
            emitUiState(errorMsg = "请输入密码")
            return
        }

        launch {
            var login = api.login(LoginRequestEntity(userName!!, password!!)).dataConvert()
            

        }

    }

}