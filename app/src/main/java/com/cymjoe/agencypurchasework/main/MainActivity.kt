package com.cymjoe.agencypurchasework.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.cymjoe.agencypurchasework.R
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_base.base.BaseActivity
import com.cymjoe.lib_base.entity.NoDataBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = ARoutePath.MainActivity)
class MainActivity : BaseActivity<NoDataBinding>() {

    private val viewModel: MainViewModel by viewModel()

    override fun getLayoutResId() = R.layout.activity_main

    override fun initView() {



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
