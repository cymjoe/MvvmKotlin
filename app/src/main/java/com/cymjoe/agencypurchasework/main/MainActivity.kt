package com.cymjoe.agencypurchasework.main

import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cymjoe.agencypurchasework.R
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_base.base.BaseActivity
import com.cymjoe.lib_base.entity.NoDataBinding
import com.cymjoe.lib_base.launchOver
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = ARoutePath.MainActivity)
class MainActivity : BaseActivity<NoDataBinding>() {

    private val viewModel: MainViewModel by viewModel()

    override fun getLayoutResId() = R.layout.activity_main

    override fun initView() {

        ARouter.getInstance()
            .build(ARoutePath.LoginActivity)
            .withString("url", "2").navigation()
    }

    override fun initData() {

    }

    override fun startObserve() {

    }


}
