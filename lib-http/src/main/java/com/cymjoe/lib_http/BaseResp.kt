package com.cymjoe.lib_http

import androidx.annotation.Keep

@Keep
data class BaseResp<T>(
    var code: Int = 0,
    var message: String = "",
    var desc: String = "",
    var data: T


)


/*数据解析扩展函数*/
fun <T> BaseResp<T>.dataConvert(): T {
    if (code == 1) {
        return data
    } else {
        throw  APIException(code, message)
    }
}