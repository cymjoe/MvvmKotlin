package com.cymjoe.moudle_login.login

import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.cymjoe.lib_base.constant.Constant
import com.cymjoe.lib_base.log
import com.cymjoe.lib_base.retrofit.RetrofitClient
import com.cymjoe.lib_base.toast
import com.cymjoe.lib_http.BaseViewModel
import com.cymjoe.moudle_login.LoginService
import com.cymjoe.moudle_login.R


class LoginViewModel : BaseViewModel() {


    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()

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


    }

}