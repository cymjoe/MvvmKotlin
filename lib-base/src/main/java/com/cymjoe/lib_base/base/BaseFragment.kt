package com.cymjoe.lib_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cymjoe.lib_base.R

import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus

/**
 * Created by cymjoe
 * on 2019/6/10 10:47
 */
abstract class BaseFragment<T : ViewDataBinding>(useBinding: Boolean = false) :
    androidx.fragment.app.Fragment(),
    CoroutineScope by MainScope() {
    private val _useBinding = useBinding
    private lateinit var mBinding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (registerEvent()) {

            EventBus.getDefault().register(this)
        }
        return if (_useBinding) {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
            mBinding.root
        } else inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        startObserve()

        initData()

    }

    open fun registerEvent(): Boolean {
        return false
    }

    private var dialog: KProgressHUD? = null

    protected open fun showLoadingDialog() {
        dialog = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(getString(R.string.string_load_msg)) /*.setDetailsLabel("Downloading data")*/
            .setCancellable(true)
            .setAnimationSpeed(1)
            .setDimAmount(0.4f)
            .show();

    }

    protected open fun dismissLoadingDialog() {

        if (dialog != null) dialog!!.dismiss()
        dialog = null
    }

    fun loading(load: Boolean) {
        if (load) {
            showLoadingDialog()
        } else {
            dismissLoadingDialog()
        }


    }

    abstract fun getLayoutResId(): Int

    abstract fun initView()

    abstract fun initData()

    abstract fun startObserve()
    override fun onDestroy() {
        if (registerEvent()) {
            EventBus.getDefault().unregister(this)
        }
        cancel()
        super.onDestroy()
    }
}