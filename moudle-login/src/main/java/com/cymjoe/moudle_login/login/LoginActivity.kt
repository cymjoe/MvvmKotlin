package com.cymjoe.moudle_login.login

import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_base.base.BaseActivity
import com.cymjoe.lib_base.constant.Constant
import com.cymjoe.lib_base.constant.MMVKConstant
import com.cymjoe.lib_base.isNoEmpty
import com.cymjoe.lib_base.log
import com.cymjoe.lib_base.mmkv
import com.cymjoe.lib_base.toast
import com.cymjoe.moudle_login.R
import com.cymjoe.moudle_login.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


@Route(path = ARoutePath.LoginActivity)
class LoginActivity : BaseActivity<ActivityLoginBinding>(true) {

    private val viewModel: LoginViewModel by viewModel()

    override fun getLayoutResId() = R.layout.activity_login

    override fun initView() {
        mBinding.viewModel = viewModel

    }

    override fun initData() {

    }

    override fun startObserve() {
        viewModel.apply {
            uiState.observe(this@LoginActivity, Observer {
                toast(it.errorMsg)
            })
            mException.observe(this@LoginActivity, Observer {
                toast(it.msg)
            })
            token.observe(this@LoginActivity, Observer {
                mmkv().putString(MMVKConstant.TOKEN, it)
                mmkv().getString(MMVKConstant.TOKEN, "")?.let { it1 -> toast(it1) }
            })
        }
    }
}
