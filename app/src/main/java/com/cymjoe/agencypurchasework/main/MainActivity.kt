package com.cymjoe.agencypurchasework.main

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.FragmentUtils
import com.cymjoe.agencypurchasework.R
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_base.base.BaseActivity
import com.cymjoe.lib_base.entity.NoDataBinding
import com.cymjoe.lib_base.toLogin
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = ARoutePath.MainActivity)
class MainActivity : BaseActivity<NoDataBinding>() {

    private val viewModel: MainViewModel by viewModel()

    override fun getLayoutResId() = R.layout.activity_main

    override fun initView() {

        toLogin()

    }

    override fun initData() {

    }

    override fun startObserve() {
        viewModel.apply {
            uiState.value?.let {
                loading(it.loading)
            }
        }

    }


}
