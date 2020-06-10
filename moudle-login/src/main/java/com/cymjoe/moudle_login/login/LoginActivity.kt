package com.cymjoe.moudle_login.login

import android.util.Log
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_base.base.BaseActivity
import com.cymjoe.lib_base.log
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
                it.errorMsg?.let { it1 -> ToastUtils.showShort(it1) }
            })

        }
    }
}
