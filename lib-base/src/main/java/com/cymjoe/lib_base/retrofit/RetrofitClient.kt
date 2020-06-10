package com.cymjoe.lib_base.retrofit

import android.os.Environment
import com.blankj.utilcode.util.SPUtils
import com.cymjoe.lib_http.BaseRetrofit
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.*
import okhttp3.Interceptor.Chain
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext


class RetrofitClient(private var baseUrl: String) : BaseRetrofit() {

    init {
        createRetrofit()
    }

    private fun createRetrofit() {
        try {
            val map = HashMap<String, String>()
            map["token"] = SPUtils.getInstance().getString("token")
            val createOkHttpClient = createOkHttpClient(map)
            val builder = Retrofit.Builder()
            retrofit = builder
                .client(createOkHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
