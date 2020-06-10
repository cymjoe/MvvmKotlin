package com.cymjoe.moudle_home.home

import com.alibaba.android.arouter.facade.annotation.Route
import com.cymjoe.lib_aroute.ARoutePath
import com.cymjoe.lib_base.base.BaseFragment
import com.cymjoe.lib_base.entity.NoDataBinding
import com.cymjoe.moudle_home.R
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = ARoutePath.HomeFragment)
class HomeFragment : BaseFragment<NoDataBinding>() {

    private val viewModel: HomeViewModel by viewModel()

    override fun getLayoutResId() = R.layout.fragment_home

    override fun initView() {

    }

    override fun initData() {

    }

    override fun startObserve() {

    }


}
