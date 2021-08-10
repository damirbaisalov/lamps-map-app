package kz.bfgroup.formmap.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

object ApiRetrofit {

    const val BASE_URL = "https://doro.kz/api/"

    val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient

    fun getApiClient(): ApiClient {
        val apiRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

//        HttpsURLConnection.setDefaultHostnameVerifier(NullHostNameVerifier())
//        val context: SSLContext = SSLContext.getInstance("TLS")
//        context.init(null,null, SecureRandom())
//        HttpsURLConnection.setDefaultSSLSocketFactory(context.socketFactory)

        return apiRetrofit.create(ApiClient::class.java)
    }

//        private fun getOkHttp(): OkHttpClient {
//        val okHttpClient = OkHttpClient.Builder()
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//
//        return okHttpClient.build()
//    }

}