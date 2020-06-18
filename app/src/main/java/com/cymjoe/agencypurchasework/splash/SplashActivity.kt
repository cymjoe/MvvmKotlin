package com.cymjoe.agencypurchasework.splash

import android.os.Handler
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.cymjoe.agencypurchasework.R
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_base.base.BaseActivity
import com.cymjoe.lib_base.entity.NoDataBinding
import com.cymjoe.lib_base.launchOver
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = ARoutePath.SplashActivity)
class SplashActivity : BaseActivity<NoDataBinding>() {

    private val viewModel: SplashViewModel by viewModel()
    override fun getLayoutResId() = R.layout.activity_splash

    override fun initView() {


    }

    override fun initData() {
        Handler().postDelayed({
            viewModel.getPermission()
        }, 1000)

    }

    override fun startObserve() {
        viewModel.apply {
            hasPermission.observe(this@SplashActivity, Observer {
                if (it) {
                    launchOver(ARoutePath.MainActivity)
                }
            })
        }

    }
}


