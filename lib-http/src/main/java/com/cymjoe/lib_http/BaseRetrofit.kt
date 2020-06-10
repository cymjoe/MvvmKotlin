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


/**
 * ll
 * Created by LiRui on 2017/9/1.
 */

open class BaseRetrofit {
    protected var retrofit: Retrofit? = null

    private inner class UnSafeHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }

    private class UnSafeTrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    private class MyTrustManager @Throws(NoSuchAlgorithmException::class, KeyStoreException::class)
    constructor(private val localTrustManager: X509TrustManager) : X509TrustManager {
        private val defaultTrustManager: X509TrustManager?

        init {
            val var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            var4.init(null as KeyStore?)
            defaultTrustManager = chooseTrustManager(var4.trustManagers)
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            try {
                defaultTrustManager!!.checkServerTrusted(chain, authType)
            } catch (ce: CertificateException) {
                localTrustManager.checkServerTrusted(chain, authType)
            }

        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    lateinit var map: Map<String, String>

    lateinit var certificates: Array<InputStream>
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

        fun getSslSocketFactory(
            certificates: Array<InputStream>?,
            bksFile: InputStream?,
            password: String?
        ): SSLSocketFactory {
            try {
                val trustManagers = certificates?.let { prepareTrustManager(*it) }
                val keyManagers = prepareKeyManager(bksFile, password)
                val sslContext = SSLContext.getInstance("TLS")
                val trustManager: TrustManager?
                trustManager = if (trustManagers != null) {
                    chooseTrustManager(trustManagers)?.let { MyTrustManager(it) }
                } else {
                    UnSafeTrustManager()
                }
                sslContext.init(keyManagers, arrayOf(trustManager), SecureRandom())
                return sslContext.socketFactory
            } catch (e: NoSuchAlgorithmException) {
                throw AssertionError(e)
            } catch (e: KeyStoreException) {
                throw AssertionError(e)
            } catch (e: KeyManagementException) {
                throw AssertionError(e)
            }

        }

        private fun prepareTrustManager(vararg certificates: InputStream): Array<TrustManager>? {
            if (certificates.isEmpty()) return null
            try {
                val certificateFactory = CertificateFactory.getInstance("X.509")
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null)
                for ((index, certificate) in certificates.withIndex()) {
                    val certificateAlias = index.toString()
                    keyStore.setCertificateEntry(
                        certificateAlias,
                        certificateFactory.generateCertificate(certificate)
                    )
                    try {
                        certificate.close()
                    } catch (e: IOException) {
                    }

                }
                val trustManagerFactory: TrustManagerFactory? =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory!!.init(keyStore)
                return trustManagerFactory.trustManagers
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null

        }

        private fun prepareKeyManager(
            bksFile: InputStream?,
            password: String?
        ): Array<KeyManager>? {
            try {
                if (bksFile == null || password == null) return null
                val clientKeyStore = KeyStore.getInstance("BKS")
                clientKeyStore.load(bksFile, password.toCharArray())
                val keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                keyManagerFactory.init(clientKeyStore, password.toCharArray())
                return keyManagerFactory.keyManagers
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
            for (trustManager in trustManagers) {
                if (trustManager is X509TrustManager) {
                    return trustManager
                }
            }
            return null
        }
    }
}
