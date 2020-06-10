package com.cymjoe.lib_http


import android.os.Build
import android.os.Environment
import android.util.Log
import okhttp3.*


import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.KeyManager
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

import okhttp3.internal.cache.CacheInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.*



open class BaseRetrofit {
    protected var retrofit: Retrofit? = null

    lateinit var map: Map<String, String>


    protected fun createOkHttpClient(map: Map<String, String>): OkHttpClient {
        val builder = OkHttpClient.Builder()
        try {
            /**
             * 获取缓存
             */
            val baseInterceptor = Interceptor { chain ->
                val request: Request = chain.request()
                val newBuilder = request.newBuilder()
                newBuilder.cacheControl(
                    CacheControl.Builder().maxStale(50 * 50 * 10, TimeUnit.SECONDS).build()
                )
                chain.proceed(newBuilder.build())
            }
            val max = 10 * 1024 * 1024L;//设置缓存最大内存
            val file =
                File(Environment.getDownloadCacheDirectory(), "cache");//为缓存设置缓存//目录文件夹 和缓存文件夹名字
            val cache = Cache(file, max);//Cache是缓存对象
            this.map = map
            builder.cache(cache)
            builder.connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(RequestInterceptor())
                .addInterceptor(baseInterceptor)
                .addNetworkInterceptor(LoggingInterceptor())
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return builder.build()
    }

    fun <T> create(clazz: Class<T>): T {
        return retrofit!!.create(clazz)
    }

    /**
     * 请求固定参数类
     */
    protected inner class RequestInterceptor() : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {


            val builder = chain.request()
                .newBuilder()
            map.forEach {
                builder.addHeader(it.key, it.value)
            }
            builder.addHeader("Content-Type", "application/json; charset=UTF-8")

            return chain.proceed(builder.build())
        }
    }

    /**
     * 日志类
     */
    protected inner class LoggingInterceptor : Interceptor {
        @Throws(Exception::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            val request = chain.request()
            val t1 = System.nanoTime()//请求发起的时间
            Log.d(TAG, String.format("发送请求 %s ", request.url()))
            val h = request.headers()
            for (s in h.names()) {
                Log.d(TAG, s + "->" + h[s])
            }

            val response = chain.proceed(request)
            try {

                val t2 = System.nanoTime()//收到响应的时间
                val responseBody = response.peekBody((1024 * 1024).toLong())
                Log.d(
                    TAG, String.format(
                        "接收响应: [%s] %n返回json:【%s】 %.1fms%n%s".toLowerCase(Locale.ROOT),
                        response.request().url(),
                        responseBody.string(),
                        (t2 - t1) / 1e6,

                        response.headers()
                    ) + "code:" + response.code()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return response
        }
    }

    companion object {
        private const val TAG = "NetWork"








    }
}
