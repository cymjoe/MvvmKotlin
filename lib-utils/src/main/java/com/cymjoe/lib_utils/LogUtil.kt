package com.cymjoe.lib_utils

import android.util.Log
import com.readystatesoftware.systembartint.BuildConfig


/**
 * Created by Administrator on 2018-7-7.
 */


object LogUtil {
    private var showDebugLog = BuildConfig.DEBUG

    /**
     * 获取类名,方法名,行号
     *
     * @return Thread:main, at com.haier.ota.OTAApplication.onCreate(OTAApplication.java:35)
     */
    private val functionName: String?
        get() {
            try {
                val sts = Thread.currentThread().stackTrace
                for (st in sts) {
                    if (st.isNativeMethod) {
                        continue
                    }
                    if (st.className == Thread::class.java.name) {
                        continue
                    }
                    if (st.className == LogUtil::class.java.name) {
                        continue
                    }
                    return ("Thread:" + Thread.currentThread().name + ", at " + st.className + "." + st.methodName
                            + "(" + st.fileName + ":" + st.lineNumber + ")")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    //    public static boolean showDebugLog = false;

    fun i(objTag: Any, objMsg: Any?) {
        val tag = getTag(objTag)
        val msg = if (objMsg?.toString() == null) "null" else objMsg.toString()
        Log.i(tag, getMsgFormat(msg))
    }

    fun d(objTag: Any, objMsg: Any?) {
        if (showDebugLog) {
            val tag = getTag(objTag)
            val msg = if (objMsg?.toString() == null) "null" else objMsg.toString()
            Log.d(tag, getMsgFormat(msg))
        }
    }

    fun w(objTag: Any, objMsg: Any?) {
        val tag = getTag(objTag)
        val msg = if (objMsg?.toString() == null) "null" else objMsg.toString()
        Log.w(tag, getMsgFormat(msg))
    }

    fun e(objTag: Any, objMsg: Any?) {
        val tag = getTag(objTag)
        val msg = if (objMsg?.toString() == null) "null" else objMsg.toString()
        Log.e(tag, getMsgFormat(msg))
    }

    private fun getTag(objTag: Any): String {
        return when (objTag) {
            is String -> {
                objTag
            }
            is Class<*> -> {
                objTag.simpleName
            }
            else -> {
                objTag.javaClass.simpleName
            }
        }
    }

    private fun getMsgFormat(msg: String): String {
        return "$msg----$functionName"
    }


}