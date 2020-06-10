package com.cymjoe.lib_http


open class APIException
/**
 * Http异常处理
 *
 * @param code    异常编码
 * @param msg 错误信息
 */
    (val code: Int, val msg: String) : Exception(Throwable(msg))
