package com.cymjoe.lib_base.base


import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cymjoe.lib_base.R
import com.cymjoe.lib_base.entity.FinishActivityEvent
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by cymjoe
 * on 2019/5/31 15:44
 */
abstract class BaseActivity<T : ViewDataBinding>(useBinding: Boolean = false) :
    AppCompatActivity(),
    CoroutineScope by MainScope() {

    private val _useBinding = useBinding
    lateinit var mBinding: T

    /**
     * 避免从桌面启动程序后，会重新实例化入口类的activity
     */
    private fun avoidLauncherAgain() {
        if (!this.isTaskRoot) { // 判断当前activity是不是所在任务栈的根
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                    finish()
                }
            }
        }
    }

    private var dialog: KProgressHUD? = null

    protected open fun showLoadingDialog() {
        dialog = KProgressHUD.create(this)
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


    /**
     * 全屏
     */
    open fun allScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    /**
     * 重写 getResource 方法，防止系统字体影响
     */
    override fun getResources(): Resources? {//禁止app字体大小跟随系统字体大小调节
        val resources = super.getResources()
        if (resources != null && resources.configuration.fontScale != 1.0f) {
            val configuration = resources.configuration
            configuration.fontScale = 1.0f
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return resources
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (_useBinding) mBinding =
            DataBindingUtil.setContentView(this, getLayoutResId()) else setContentView(
            getLayoutResId()
        )
        avoidLauncherAgain()
        EventBus.getDefault().register(this)
        initView()
        initData()
        startObserve()
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

        EventBus.getDefault().unregister(this)

        super.onDestroy()
        cancel()
    }

    @Subscribe
    public fun finishActivity(event: FinishActivityEvent) {
        if (event.activityName == this::class.java.simpleName) {
            finish()
        }
    }
}


