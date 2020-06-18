package com.cymjoe.lib_http

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


object NetManager {
    var application: Application? = null

    //连接超时时间(秒)
    private var timeOut: Long = 5

    //读取超时时间(秒)
    private var readOut: Long = 5
    private var retrofit: Retrofit? = null
    var builder: Retrofit.Builder? = null
    var okBuilder: OkHttpClient.Builder? = null


    fun Builder(): NetManager {
        if (builder == null) {
            builder = Retrofit.Builder()

        }
        return this
    }

    fun setTime(readOut: Int, timeOut: Int): NetManager {
        NetManager.readOut = readOut.toLong()
        NetManager.timeOut = timeOut.toLong()
        return this
    }

    /**
     * 添加请求头
     *
     * @param maps
     * @return
     */
    fun setHeader(maps: Map<String?, String?>): NetManager {
        if (okBuilder == null) {
            okBuilder = OkHttpClient.Builder()
        }
        okBuilder!!.addInterceptor { chain ->
            val builder = chain.request().newBuilder()
            for ((key, value) in maps) {
                builder.addHeader(key, value)
            }
            chain.proceed(builder.build())
        }
        return this
    }

    /**
     * 设置缓存
     *
     * @param cacheTime 缓存最大时间
     * @param max       缓存最大内存
     * @param file      缓存路径
     * @return
     */
    fun setCache(cacheTime: Int, max: Long, file: File?): NetManager {
        if (okBuilder == null) {
            okBuilder = OkHttpClient.Builder()
        }
        okBuilder!!.cache(Cache(file, max))
        okBuilder!!.addInterceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
            builder.cacheControl(
                CacheControl.Builder()
                    .maxStale(cacheTime, TimeUnit.SECONDS).build()
            )
            chain.proceed(builder.build())
        }
        return this
    }

    fun client(): NetManager {
        if (okBuilder == null) {
            okBuilder = OkHttpClient.Builder()
        }
        okBuilder!!.addNetworkInterceptor { chain ->
            val request = chain.request()
            val l = System.nanoTime()
            Log.d(
                TAG,
                String.format("发送请求 %s ", request.url())
            )
            val headers = request.headers()
            for (i in headers.names().indices) {
                Log.d(
                    TAG,
                    headers.names().toString() + "->" + headers.names().toTypedArray()[i]
                )
            }
            val response = chain.proceed(request)
            try {
                val l1 = System.nanoTime() //收到响应的时间
                val responseBody = response.peekBody((1024 * 1024).toLong())
                Log.d(
                    TAG, String.format(
                        "接收响应: [%s] %n返回json:【%s】 %.1fms%n%s".toLowerCase(Locale.ROOT),
                        response.request().url(),
                        responseBody.string(),
                        (l1 - l) / 1e6,
                        response.headers()
                    ) + "code:" + response.code()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            response
        }
        okBuilder!!.readTimeout(readOut, TimeUnit.SECONDS)
        okBuilder!!.callTimeout(timeOut, TimeUnit.SECONDS)
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create()
        builder!!.client(okBuilder!!.build())
            .addConverterFactory(GsonConverterFactory.create(gson))

        return this
    }

    fun baseUrl(url: String?): NetManager {
        builder!!.baseUrl(url)
        return this
    }

    fun build(): NetManager {
        if (retrofit == null) {
            retrofit = builder!!.build()
        }
        return this
    }

    fun <T> getService(service: Class<T>?): T {
        return retrofit!!.create(service)
    }


}